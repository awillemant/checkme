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

import io.checkme.commons.CheckmeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class JsonFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("Initializing JsonFilter");
    }


    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("Filtering request");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        final String queryString = req.getQueryString();
        final PrintWriter writer = resp.getWriter();
        final String jsonpMethodName = req.getParameter("jsonp");
        if (jsonpMethodName != null) {
            LOGGER.debug("JSONP Request");
            resp.setContentType("text/javascript");
            wrapResultInJsonP(chain, req, resp, writer, jsonpMethodName);
        } else if ("json".equals(queryString)) {
            LOGGER.debug("JSON Request");
            resp.setContentType("application/json");
            wrapResultInJson(chain, req, resp, writer);
        } else {
            LOGGER.debug("Plain Text Request");
            resp.setContentType("text/plain");
            chain.doFilter(req, resp);
        }
    }


    private void wrapResultInJson(final FilterChain chain, final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter writer) throws IOException, ServletException {
        writer.append("{\"status\":\"");
        chain.doFilter(req, resp);
        writer.append("\",\"headers\":{");
        writer.append(resp.getHeaderNames().stream()
                .filter(h -> h.startsWith(CheckmeConstants.HEADER_PREFIX))
                .map(h -> "\"" + h.substring(CheckmeConstants.HEADER_PREFIX.length()) + "\":\"" + resp.getHeader(h) + "\"")
                .collect(Collectors.joining(",")));
        writer.append("}}");
    }


    private void wrapResultInJsonP(final FilterChain chain, final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter writer, final String jsonpMethodName)
            throws IOException, ServletException {
        writer.append(jsonpMethodName).append("(");
        wrapResultInJson(chain, req, resp, writer);
        writer.append(");");
    }


    @Override
    public void destroy() {
        LOGGER.debug("Destroying JsonFilter");
    }
}
