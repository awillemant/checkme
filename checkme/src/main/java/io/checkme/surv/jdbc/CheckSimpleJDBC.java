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
