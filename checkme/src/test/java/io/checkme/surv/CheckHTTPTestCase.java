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

import io.checkme.tests.utils.TestUtils;
import org.h2.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import io.checkme.servlet.CheckmeServlet;
import io.checkme.surv.handler.OKHttpResponseHandler;
import io.checkme.utils.UrlProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckHTTPTestCase extends AbstractCheckmeTestCase {

    private static final String PROPERTIES_PATH = "checkme_routes_http.properties";

    private static final String HTTP_ROUTE_PATH = "/http";

    private static final String HTTP_ROUTE = "http";

    private static final String URLPROVIDER_FIELDNAME = "urlProvider";

    @Mock
    private UrlProvider mockedUrlProvider;

    @Mock
    private URL mockedUrl;

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;


    @Before
    public void setUp() throws Exception {
        when(mockedUrlProvider.provideUrl(anyString())).thenReturn(mockedUrl);
        when(mockedUrl.openConnection()).thenReturn(mockedHttpUrlConnection);
    }


    @Test
    public void shouldWriteOKInResponse() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedHttpUrlConnection.getResponseCode()).thenReturn(200);
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(IOUtils.getInputStreamFromString("OK"));
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("OK"));
    }

    @Test
    public void shouldWriteKOInResponseBecauseOfContent() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedHttpUrlConnection.getResponseCode()).thenReturn(200);
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(IOUtils.getInputStreamFromString("not ok"));
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKOInResponseBecauseOfStatusCode() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedHttpUrlConnection.getResponseCode()).thenReturn(500);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKOInResponseBecauseOfBadUrl() throws Exception {
        // GIVEN
        when(mockedUrlProvider.provideUrl(anyString())).thenThrow(new MalformedURLException());
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKOInResponseBecauseOfBadConnection() throws Exception {
        // GIVEN
        when(mockedUrl.openConnection()).thenThrow(new IOException());
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKOInResponseBecauseCannotReach() throws Exception {
        // GIVEN
        doThrow(new IOException()).when(mockedHttpUrlConnection).connect();
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(HTTP_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(URLPROVIDER_FIELDNAME, mockedUrlProvider, servlet.getStrategies().get(HTTP_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldUseDefaultHandlerIfClassNotPresent() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        servlet.init(servletConfiguration);
        // WHEN
        final Object privateField = TestUtils.getPrivateField("handler", servlet.getStrategies().get("http2"));
        //THEN
        assertThat(privateField.getClass()).isEqualTo(OKHttpResponseHandler.class);
    }


}
