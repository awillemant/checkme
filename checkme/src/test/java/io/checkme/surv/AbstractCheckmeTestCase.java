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

import org.h2.util.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import io.checkme.servlet.CheckmeServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCheckmeTestCase {

    @Mock
    protected ServletConfig servletConfiguration;

    @Mock
    protected ServletContext servletContext;

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected PrintWriter printWriter;

    public CheckmeServlet initCheckmeServlet(String routesPath) {
        return initCheckmeServlet(routesPath, IOUtils.getInputStreamFromString(""));
    }

    protected CheckmeServlet initCheckmeServlet(String routesPath, InputStream servletContextMockGetResourceAsStream) {
        CheckmeServlet servlet = new CheckmeServlet();
        when(servletConfiguration.getInitParameter(eq("routesProperties"))).thenReturn(routesPath);
        when(servletConfiguration.getServletContext()).thenReturn(servletContext);
        return servlet;
    }

}
