package io.checkme.surv.handlers;

/*-
 * #%L
 * checkme-http-handlers
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.checkme.surv.handler.HttpResponseHandler;
import io.checkme.utils.HTTPContentProvider;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ElasticSearchHttpResponseHandler implements HttpResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchHttpResponseHandler.class);
    private static final String EXPECTED_TAGLINE = "You Know, for Search";
    private static final String TAGLINE_JSON_KEY = "tagline";

    private ObjectMapper objectMapper;

    public ElasticSearchHttpResponseHandler() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean handle(HttpURLConnection connection) {
        try {
            HTTPContentProvider httpContentProvider = new HTTPContentProvider();
            JsonNode rootNode = objectMapper.readTree(httpContentProvider.provideHTTPContent(connection));
            return EXPECTED_TAGLINE.equals(rootNode.get(TAGLINE_JSON_KEY).asText());
        } catch (IOException e) {
            LOGGER.error("Failed to reach Elastic Search :", e);
            return false;
        }
    }
}
