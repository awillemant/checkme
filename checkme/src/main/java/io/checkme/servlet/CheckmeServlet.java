package io.checkme.servlet;

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


import io.checkme.exception.RouteFormatException;
import io.checkme.surv.AbstractCheckme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CheckmeServlet extends HttpServlet {

    private static final int KEY_PARTS_NB = 3;

    private static final long serialVersionUID = 2543285465280841067L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckmeServlet.class);

    private static final String ROUTES_PROPERTIES_PARAM = "routesProperties";

    private static final String KEY_PATH_SUFFIX = "path";

    private static final String KEY_CLASS_SUFFIX = "class";

    private static final String KEY_CONFIG_SUFFIX = "config";

    private static final String KEY_HEADER_SUFFIX = "header";

    private static final String KEY_PREFIX = "route";

    private String propertiesFilePath;

    private Map<String, AbstractCheckme> strategies;

    public CheckmeServlet() {
        super();
    }

    @Override
    public void init(ServletConfig configuration) throws ServletException {
        LOGGER.info("Initialization of CheckmeServlet");
        super.init();
        propertiesFilePath = configuration.getInitParameter(ROUTES_PROPERTIES_PARAM);
        strategies = new HashMap<>();
        Properties routes = loadProperties();
        parseRoutes(routes, configuration.getServletContext());
        parseConfigs(routes);
        parseHeaders(routes);
    }

    private Properties loadProperties() throws ServletException {
        LOGGER.info("Reading properties");
        Properties routes = new Properties();
        try (InputStream routesProperties = this.getClass().getClassLoader().getResourceAsStream(propertiesFilePath)) {
            if (routesProperties == null) {
                String message = "This file does not seem to exist " + propertiesFilePath;
                LOGGER.error(message);
                throw new ServletException(message);
            }
            routes.load(routesProperties);
        } catch (IOException e) {
            LOGGER.error("Impossible to read {} ", propertiesFilePath);
            throw new ServletException("Impossible to read " + propertiesFilePath, e);
        }
        return routes;
    }

    private void parseRoutes(Properties routes, ServletContext servletContext) {
        LOGGER.debug("Parsing routes");
        Enumeration<?> keys = routes.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            try {
                String[] keyInfo = parseKey(key);
                if (keyInfo[2].equals(KEY_PATH_SUFFIX)) {
                    String path = routes.getProperty(key);
                    String classKey = key.replaceAll(KEY_PATH_SUFFIX, KEY_CLASS_SUFFIX);
                    String className = routes.getProperty(classKey);
                    AbstractCheckme checkmeInstance = buildFromClass(path, className, servletContext);
                    strategies.put(keyInfo[1], checkmeInstance);
                }
            } catch (RouteFormatException | ClassNotFoundException e) {
                LOGGER.warn("Error while reading properties", e);
            }
        }
    }

    private void parseConfigs(Properties routes) {
        LOGGER.debug("Parsing configs");
        Enumeration<?> keys = routes.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            try {
                String[] keyInfo = parseKey(key);
                if (keyInfo[2].equals(KEY_CONFIG_SUFFIX)) {
                    String value = routes.getProperty(key);
                    strategies.get(keyInfo[1]).addConfiguration(keyInfo[3], value);
                }
            } catch (RouteFormatException e) {
                LOGGER.warn("Error while reading configuration", e);
            }
        }
        for (Map.Entry<String, AbstractCheckme> strategy : strategies.entrySet()) {
            strategies.get(strategy.getKey()).refreshConfiguration();
        }
    }

    private void parseHeaders(Properties routes) {
        LOGGER.debug("Parsing headers");
        Enumeration<?> keys = routes.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            try {
                String[] keyInfo = parseKey(key);
                if (keyInfo[2].equals(KEY_HEADER_SUFFIX)) {
                    String value = routes.getProperty(key);
                    strategies.get(keyInfo[1]).addHeader(keyInfo[3], value);
                }
            } catch (RouteFormatException e) {
                LOGGER.warn("Error while reading header", e);
            }
        }
        for (Map.Entry<String, AbstractCheckme> strategy : strategies.entrySet()) {
            strategies.get(strategy.getKey()).refreshConfiguration();
        }
    }

    private String[] parseKey(String key) throws RouteFormatException {
        LOGGER.debug("Parsing Key : {}", key);
        validateKey(key);
        return key.split("\\.");
    }

    private void validateKey(String key) throws RouteFormatException {
        String[] keyInfo = key.split("\\.");
        String message = "Unrecognized Key in routes";
        if (keyInfo.length < KEY_PARTS_NB) {
            LOGGER.warn("'{}' does not contains at least {} parts", key, KEY_PARTS_NB);
            throw new RouteFormatException(message, key);
        }
        if (!keyInfo[0].equals(KEY_PREFIX)) {
            LOGGER.warn("'{}' does not begin by '{}.'", key, KEY_PREFIX);
            throw new RouteFormatException(message, key);
        }
        List<String> authorizedKeys = Arrays.asList(KEY_CLASS_SUFFIX, KEY_PATH_SUFFIX, KEY_CONFIG_SUFFIX, KEY_HEADER_SUFFIX);
        if (!authorizedKeys.contains(keyInfo[2])) {
            LOGGER.warn("'{}' does not end by {}", key, String.join(", ", authorizedKeys));
            throw new RouteFormatException(message, key);
        }

    }

    private AbstractCheckme buildFromClass(String path, String className, ServletContext servletContext) throws ClassNotFoundException {
        Class<?> classDefinition = Class.forName(className);
        if (!hasSuperclass(classDefinition, AbstractCheckme.class)) {
            String message = "'" + className + "' does not extend AbstractCheckme";
            LOGGER.warn(message);
            throw new ClassNotFoundException(message);
        } else {
            try {
                Constructor<?> constructor = classDefinition.getConstructor(String.class, ServletContext.class);
                return (AbstractCheckme) constructor.newInstance(path, servletContext);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
                LOGGER.warn("'{}' impossible to instanciate", className);
                throw new ClassNotFoundException("'" + className + "' impossible to instanciate", e);
            }
        }
    }

    private boolean hasSuperclass(Class<?> classe, Class<?> superclasse) {
        while (!classe.getSuperclass().equals(Object.class)) {
            if (classe.getSuperclass().equals(superclasse)) {
                return true;
            }
            classe = classe.getSuperclass();
        }
        return false;
    }

    public Map<String, AbstractCheckme> getStrategies() {
        return strategies;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doCORS(resp);
        boolean found = false;
        String pathInfo = req.getPathInfo();
        LOGGER.debug("Looking for a checkme responding to : {}", pathInfo);

        for (AbstractCheckme checkme : strategies.values()) {
            if (pathInfo.equals(checkme.getPath())) {
                LOGGER.debug("Found a checkme : {}", checkme.getClass().getSimpleName());
                found = true;
                try {
                    checkme.check(req, resp);
                } catch (IOException ioe) {
                    LOGGER.error("Found a checkme responding to {}, but failed to check it", pathInfo, ioe);
                }
            }
        }

        if (!found) {
            try {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException ioe) {
                LOGGER.error("Failed to send error, did not found checkme responding to : {}", pathInfo, ioe);
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder json = new StringBuilder(800);
        //json.append("{\"selfname\" : \""+this.selfName+"\",");
        json.append("\"checkers\" :[");

        if(req.getQueryString().equals("discovery")){
            doCORS(resp);
            try {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                Iterator strategiesIterator = strategies.entrySet().iterator();
                while (strategiesIterator.hasNext()) {
                    Map.Entry checker = (Map.Entry) strategiesIterator.next();
                    AbstractCheckme ws = (AbstractCheckme) checker.getValue();
                    json.append("{\n\t\"name\" : \"" + checker.getKey() + "\", " + ws.toDescribe() + "\t},");

                }
                json.delete(json.length() - 1,json.length());
                json.append("]}");
                resp.getWriter().write(json.toString());
            }
            catch (IOException ioe){
                LOGGER.error("Discovery doesn't work");
            }
        }


    }



    private void doCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Expose-Headers", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH,OPTIONS");
    }
}
