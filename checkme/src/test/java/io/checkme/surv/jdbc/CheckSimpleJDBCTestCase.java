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

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import org.h2.util.IOUtils;
import org.junit.Test;

import io.checkme.servlet.CheckmeServlet;
import io.checkme.surv.AbstractCheckmeTestCase;

public class CheckSimpleJDBCTestCase extends AbstractCheckmeTestCase {

    @Test
    public void shouldWriteOKInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_simpleJDBC.properties", IOUtils.getInputStreamFromString(""));
        when(request.getPathInfo()).thenReturn("/jdbcOk");
        when(response.getWriter()).thenReturn(printWriter);
        // WHEN
        try {
            servlet.init(servletConfiguration);
            servlet.doGet(request, response);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("OK"));
    }

    @Test
    public void shouldWriteKOBecauseOfDriver() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_withIncorrectDriver.properties");
        when(request.getPathInfo()).thenReturn("/jdbc");
        when(response.getWriter()).thenReturn(printWriter);
        // WHEN
        try {
            servlet.init(servletConfiguration);
            servlet.doGet(request, response);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldCrashBecauseOfBadKey() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_withBadKey.properties");
        try {
            // WHEN
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // THEN
            assertThat(e).hasMessage("Impossible to get property : jdbc.dataSource.driver pour la key : driverKey");
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
    }

    @Test
    public void shouldCrashBecauseOfMissingKey() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_withMissingKey.properties");
        try {
            // WHEN
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // THEN
            assertThat(e.getMessage()).isEqualTo("Missing Key in configuration : driverKey");
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
    }

    @Test
    public void shouldWriteKOBecauseOfSQL() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_simpleJDBC.properties", IOUtils.getInputStreamFromString(""));
        when(request.getPathInfo()).thenReturn("/jdbcOk");
        when(response.getWriter()).thenReturn(printWriter);
        // WHEN
        try {
            servlet.init(servletConfiguration);
            CheckSimpleJDBC jdbcCheckService = (CheckSimpleJDBC) servlet.getStrategies().get("jdbcOk");
            jdbcCheckService.setSqlRequest("SELECT 2");
            servlet.doGet(request, response);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldCrashBecauseOfProperties() {

        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_withFileNotExistingFile.properties", null);
        try {
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("This file does not seem to exist test.properties");
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
    }
}
