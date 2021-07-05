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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import io.checkme.surv.handler.HttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchHttpResponseHandlerTestCase {

    private static final String ELASTIC_SEARCH_TAGLINE = "{\"name\" : \"NIp085y\",\"tagline\" : \"You Know, for Search\"}";
    private static final String ELASTIC_SEARCH_WRONG_TAGLINE = "{\"name\" : \"NIp085y\",\"tagline\" : \"You Know, for Test\"}";

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;

    @Test
    public void shouldReturnTrue() throws IOException {
        //GIVEN
        InputStream elasticSearchInputStream = new ByteArrayInputStream(ELASTIC_SEARCH_TAGLINE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(elasticSearchInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new ElasticSearchHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseBecauseOfContent() throws IOException {
        //GIVEN
        InputStream elasticSearchWrongInputStream = new ByteArrayInputStream(ELASTIC_SEARCH_WRONG_TAGLINE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(elasticSearchWrongInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new ElasticSearchHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseBecauseOfStreamError() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenThrow(new IOException());
        HttpResponseHandler handler = (HttpResponseHandler) new ElasticSearchHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }
}
