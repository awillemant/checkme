package io.checkme.surv;


import io.checkme.commons.CheckmeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


public abstract class AbstractCheckme {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCheckme.class);

    private static final String POSITIVE_RESPONSE = "OK";

    private static final String NEGATIVE_RESPONSE = "KO";


    protected Properties properties;

    private String path;

    private Map<String, String> configuration;

    private Map<String, String> header;

    private ServletContext servletContext;


    public AbstractCheckme(String path, ServletContext servletContext) {
        configuration = new HashMap<>();
        header = new HashMap<>();
        this.path = path;
        this.servletContext = servletContext;
    }


    public String getPath() {
        return path;
    }


    public String getMandatoryConfiguration(String key) {
        if (configuration.containsKey(key)) {
            return configuration.get(key);
        } else {
            String message = "Missing Key in configuration : " + key;
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public Optional<String> getOptionalConfiguration(String key) {
        return Optional.ofNullable(configuration.get(key));
    }


    public void addConfiguration(String key, String value) {
        configuration.put(key, value);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public void refreshConfiguration() {
        // DO NOTHING BY DEFAULT
    }


    protected abstract boolean isOk();


    private String getPositiveResponse() {
        return POSITIVE_RESPONSE;
    }


    private String getNegativeResponse() {
        return NEGATIVE_RESPONSE;
    }


    public void check(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOGGER.trace("Checking request {}", req.getPathInfo());
        if (!header.isEmpty()) {
            for (Map.Entry<String, String> headerEntry : header.entrySet()) {
                resp.setHeader(CheckmeConstants.HEADER_PREFIX + headerEntry.getKey(), headerEntry.getValue());
            }

        }
        if (isOk()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(getPositiveResponse());
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(getNegativeResponse());
        }
    }


    protected void loadProperties(String path) {
        properties = new Properties();
        try {
            InputStream propertiesInputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            if (propertiesInputStream == null) {
                propertiesInputStream = servletContext.getResourceAsStream(path);
                if (propertiesInputStream == null) {
                    String message = "This file does not seem to exist " + path;
                    LOGGER.error(message);
                    throw new IllegalArgumentException(message);
                }
            }
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            String message = "Impossible to read " + path;
            LOGGER.error(message);
            throw new IllegalArgumentException(message, e);
        }
    }


    protected String getProperty(String key) {
        String propertyKey = getMandatoryConfiguration(key);
        if (propertyKey == null) {
            String message = "Impossible to get configuration : " + key;
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        } else {
            String property = properties.getProperty(propertyKey);
            if (property == null) {
                String message = "Impossible to get property : " + propertyKey + " pour la key : " + key;
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
            } else {
                return property;
            }
        }
    }


    public String toDescribe() {
        StringBuilder describe = new StringBuilder();
        describe = describe.append("\t\t\"path\" : \""+this.getPath() + "\" ,\n");
        describe = describe.append("\t\t\"header\" : "+this.toDescribeMap(header) +" ,\n");
        describe = describe.append("\t\t\"classOfChecker\" : \""+this.getClass() + "\" ,\n");
        describe = describe.append("\t\t\"config\" : "+this.toDescribeMap(configuration) + " \n");
        return describe.toString();
    }

    public String toDescribeMap(Map m) {
        StringBuilder describe = new StringBuilder("{");
        Iterator mapIterator = m.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Map.Entry element = (Map.Entry) mapIterator.next();
            describe = describe.append("\""+element.getKey()+"\" : \""+element.getValue().toString()+"\" ,");
        }
        if(!describe.toString().equals("{")) {
            describe.delete(describe.length() - 1,describe.length());
        }
        describe = describe.append("}");

        return describe.toString();
    }
}
