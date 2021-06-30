package io.checkme.surv.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

/**
 * Created by a527968 on 07/02/2017.
 */
public class CheckSpecificRequestJDBC extends AbstractCheckJDBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSpecificRequestJDBC.class);
    private static final String SQL_REQUEST = "sqlRequest";
    private static final String SQL_RESULT = "sqlIntegerResult";

    public CheckSpecificRequestJDBC(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckSpecificRequestJDBC with path:{}", path);
    }

    @Override
    String getSqlRequest() {
        return getMandatoryConfiguration(SQL_REQUEST);
    }

    @Override
    Logger getLogger() {
        return LoggerFactory.getLogger(CheckSpecificRequestJDBC.class);
    }

    @Override
    int getSqlResult() {
        return Integer.parseInt(getMandatoryConfiguration(SQL_RESULT));
    }
}
