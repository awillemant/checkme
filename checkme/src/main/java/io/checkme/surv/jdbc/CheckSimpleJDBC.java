package io.checkme.surv.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class CheckSimpleJDBC extends AbstractCheckJDBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSimpleJDBC.class);

    private String sqlRequest = "SELECT 1";


    public CheckSimpleJDBC(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckSimpleJDBC with path:{}", path);
        setSqlRequest(sqlRequest);
    }

    void setSqlRequest(String sqlRequest) {
        this.sqlRequest = sqlRequest;
    }

    @Override
    String getSqlRequest() {
        return sqlRequest;
    }

    @Override
    Logger getLogger() {
        return LoggerFactory.getLogger(CheckSimpleJDBC.class);
    }


    @Override
    int getSqlResult() {
        String sqlResult = "1";
        return Integer.parseInt(sqlResult);
    }
}
