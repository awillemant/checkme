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
