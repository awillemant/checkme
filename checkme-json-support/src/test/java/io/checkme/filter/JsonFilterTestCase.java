package io.checkme.filter;

/*-
 * #%L
 * checkme-json-support
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonFilterTestCase {

    @Mock
    private ServletConfig servletConfiguration;

    @Mock
    private ServletContext servletContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private FilterChain filterChain;


    @Before
    public void setUp() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(printWriter.append(any())).thenReturn(printWriter);
    }


    @Test
    public void shouldNotAlterResult() throws Exception {
        //GIVEN
        JsonFilter jsonFilter = new JsonFilter();
        jsonFilter.init(mock(FilterConfig.class));
        when(request.getQueryString()).thenReturn("");
        when(request.getParameter("jsonp")).thenReturn(null);
        //WHEN
        jsonFilter.doFilter(request, response, filterChain);
        jsonFilter.destroy();
        //THEN
        verify(printWriter, never()).append(any());
        verify(filterChain,times(1)).doFilter(eq(request),eq(response));
        verify(response,times(1)).setContentType(eq("text/plain"));
    }

    @Test
    public void shouldWrapResultInJson() throws Exception {
        //GIVEN
        JsonFilter jsonFilter = new JsonFilter();
        jsonFilter.init(mock(FilterConfig.class));
        when(request.getQueryString()).thenReturn("json");
        when(request.getParameter("jsonp")).thenReturn(null);
        //WHEN
        jsonFilter.doFilter(request, response, filterChain);
        jsonFilter.destroy();
        //THEN
        verify(printWriter, times(1)).append(eq("{\"status\":\""));
        verify(filterChain,times(1)).doFilter(eq(request),eq(response));
        verify(response,times(1)).setContentType(eq("application/json"));
    }

    @Test
    public void shouldWrapResultInJsonP() throws Exception {
        //GIVEN
        JsonFilter jsonFilter = new JsonFilter();
        jsonFilter.init(mock(FilterConfig.class));
        when(request.getQueryString()).thenReturn("jsonp=foobarMethod");
        when(request.getParameter(eq("jsonp"))).thenReturn("foobarMethod");
        //WHEN
        jsonFilter.doFilter(request, response, filterChain);
        jsonFilter.destroy();
        //THEN
        verify(printWriter, times(1)).append(eq("foobarMethod"));
        verify(filterChain,times(1)).doFilter(eq(request),eq(response));
        verify(response,times(1)).setContentType(eq("text/javascript"));
    }
}
