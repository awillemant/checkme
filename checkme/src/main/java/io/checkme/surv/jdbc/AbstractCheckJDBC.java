package io.checkme.surv.jdbc;

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

import io.checkme.surv.AbstractCheckme;
import org.slf4j.Logger;

import javax.servlet.ServletContext;
import java.sql.*;

/**
 * Created by a527968 on 07/02/2017.
 */
public abstract class AbstractCheckJDBC extends AbstractCheckme {

    private static final String PSWD_KEY_CONFIG_KEY = "passwordKey";

    private static final String USER_KEY_CONFIG_KEY = "userKey";

    private static final String URL_KEY_CONFIG_KEY = "urlKey";

    private static final String DRIVER_KEY_CONFIG_KEY = "driverKey";

    private static final String PROPERTIES_PATH_CONFIG_KEY = "propertiesPath";
    private String serverURL;
    private String username;
    private String password;
    private String driver;
    private Connection connection;
    private Statement statement;
    private ResultSet results;

    AbstractCheckJDBC(String path, ServletContext servletContext) {
        super(path, servletContext);
    }

    abstract String getSqlRequest();

    abstract Logger getLogger();

    abstract int getSqlResult();


    @Override
    public void refreshConfiguration() {
        loadProperties(getMandatoryConfiguration(PROPERTIES_PATH_CONFIG_KEY));
        parseJDBCProperties();
    }

    private void parseJDBCProperties() {
        driver = getProperty(DRIVER_KEY_CONFIG_KEY);
        serverURL = getProperty(URL_KEY_CONFIG_KEY);
        username = getProperty(USER_KEY_CONFIG_KEY);
        password = getProperty(PSWD_KEY_CONFIG_KEY);
    }

    private void closeConnection() throws SQLException {
        results.close();
        statement.close();
        connection.close();
    }

    private Connection createConnection() throws SQLException {
        try {
            Class<?> c = Class.forName(driver);
            Driver pilote = (Driver) c.newInstance();
            DriverManager.registerDriver(pilote);
            return DriverManager.getConnection(serverURL, username, password);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            String message = "Error while looking for JDBC Driver " + driver;
            getLogger().error(message);
            throw new SQLException(message, e);
        }
    }

    private void executeSQL() throws SQLException {
        connection = createConnection();

        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sqlRequest = getSqlRequest();
        getLogger().debug("Executing SQL request {}", sqlRequest);
        results = statement.executeQuery(sqlRequest);
    }


    @Override
    public boolean isOk() {
        getLogger().debug("Executing CheckSimpleJDBC");
        try {
            executeSQL();
            results.first();
            int result = results.getInt(1);
            closeConnection();
            return result == getSqlResult();
        } catch (SQLException e) {
            getLogger().error("The request '" + getSqlRequest() + "' does not work", e);
        }
        return false;
    }
}
