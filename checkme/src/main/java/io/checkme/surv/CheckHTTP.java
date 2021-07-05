package io.checkme.surv;

/*-
 * #%L
 * checkme
 * %%
 * Copyright (C) 2013 - 2021 Apa
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.checkme.surv.handler.HttpResponseHandler;
import io.checkme.surv.handler.OKHttpResponseHandler;
import io.checkme.utils.UrlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Optional;

public class CheckHTTP extends AbstractCheckme {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckHTTP.class);

    private static final String URL_CONFIG_PARAM_KEY = "url";

    private static final String HANDLER_CONFIG_PARAM_KEY = "handler-class";

    private static final String HTTP_TIMEOUT_CONFIG_PARAM_KEY = "timeout";

    private static final String DEFAULT_TIMEOUT = "5000";

    private static final String PROXY_HOST_CONFIG_PARAM_KEY = "proxy-host";

    private static final String PROXY_PORT_CONFIG_PARAM_KEY = "proxy-port";

    private static final String PROXY_DISABLE_CONFIG_PARAM_KEY = "proxy-disable";


    private HttpResponseHandler handler;

    private UrlProvider urlProvider;

    private Proxy proxy;


    public CheckHTTP(String path, ServletContext servletContext) {
        super(path, servletContext);
        urlProvider = new UrlProvider();
        LOGGER.debug("Building CheckHTTP with path:{}", path);
    }


    @Override
    public void refreshConfiguration() {
        String handlerRef = getMandatoryConfiguration(HANDLER_CONFIG_PARAM_KEY);
        try {
            Class<HttpResponseHandler> handlerClass = (Class<HttpResponseHandler>) Class.forName(handlerRef);
            handler = handlerClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("Can't find handler : {}, using default one", handlerRef, e);
            handler = new OKHttpResponseHandler();
        }
        configureProxy();
    }


    private void configureProxy() {
        final Optional<String> proxyHostConfiguration = getOptionalConfiguration(PROXY_HOST_CONFIG_PARAM_KEY);
        final Optional<String> proxyPortConfiguration = getOptionalConfiguration(PROXY_PORT_CONFIG_PARAM_KEY);
        final Optional<String> proxyDisableConfiguraton = getOptionalConfiguration(PROXY_DISABLE_CONFIG_PARAM_KEY);
        if (proxyDisableConfiguraton.isPresent()&& Boolean.parseBoolean(proxyDisableConfiguraton.get())) {
            proxy = Proxy.NO_PROXY;
        } else if (proxyHostConfiguration.isPresent() && proxyPortConfiguration.isPresent()) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostConfiguration.get(), Integer.parseInt(proxyPortConfiguration.get())));
        }
    }


    private String getURL() {
        return getMandatoryConfiguration(URL_CONFIG_PARAM_KEY);
    }


    @Override
    protected boolean isOk() {
        return getConnection(getURL()).map(this::connect).orElse(false);
    }


    private boolean connect(HttpURLConnection connection) {
        try {
            connection.setConnectTimeout(Integer.parseInt(getOptionalConfiguration(HTTP_TIMEOUT_CONFIG_PARAM_KEY).orElse(DEFAULT_TIMEOUT)));
            connection.setRequestMethod("GET");
            //Date currentDate = new Date();
            connection.connect();
            int code = connection.getResponseCode();
            LOGGER.debug("Test Service URL : {} Response code: {}", connection.getURL(), code);
            if (code != HttpURLConnection.HTTP_OK) {
                return false;
            } else {
                return handler.handle(connection);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to reach connect to : {}", connection.getURL(), e);
            return false;
        }
    }



    private Optional<HttpURLConnection> getConnection(String url) {
        try {
            URL urlObject = urlProvider.provideUrl(url);
            if (proxy == null) {
                return Optional.of((HttpURLConnection) urlObject.openConnection());
            } else {
                return Optional.of((HttpURLConnection) urlObject.openConnection(proxy));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse URL : {}", url, e);
            return Optional.empty();
        }
    }



}


