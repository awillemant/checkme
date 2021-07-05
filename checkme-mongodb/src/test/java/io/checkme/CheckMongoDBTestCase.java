package io.checkme;

/*-
 * #%L
 * checkme-mongodb
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
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.checkme.tests.utils.TestUtils;
import io.checkme.utils.MongoClientProvider;
import io.checkme.utils.UriProvider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import io.checkme.servlet.CheckmeServlet;

public class CheckMongoDBTestCase extends AbstractCheckmeTestCase {

    private static final String PROPERTIES_PATH = "checkme_routes_mongo.properties";
    private static final String MONGO_ROUTE_PATH = "/toMongo";
    private static final String MONGO_ROUTE = "toMongo";
    private static final String MONGOCLIENT_PROVIDER_FIELDNAME = "mongoClientProvider";
    private static final String URI_PROVIDER_FIELDNAME = "uriProvider";

    @Mock
    private UriProvider mockedUriProvider;

    @Mock
    private MongoClientURI mockedMongoClientURI;

    @Mock
    private MongoClientProvider mockedMongoClientProvider;

    @Mock
    private MongoClient mockedMongoClient;

    @Mock
    private MongoDatabase mockedMongoDatabase;

    @Mock
    private MongoIterable<String> mockCollectionsNames;

    @Mock
    private MongoCursor<String> mockedStringIterator;

    @Before
    public void setUp() {
        when(mockedUriProvider.provideUri(anyString())).thenReturn(mockedMongoClientURI);
        when(mockedMongoClientProvider.provide(mockedMongoClientURI)).thenReturn(mockedMongoClient);
        when(mockCollectionsNames.iterator()).thenReturn(mockedStringIterator);
        when(mockedStringIterator.hasNext()).thenReturn(true, true, true, false);
    }

    @Test
    public void shouldWriteOKInResponse() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(MONGO_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedMongoClient.getDatabase(anyString())).thenReturn(mockedMongoDatabase);
        when(mockedMongoDatabase.listCollectionNames()).thenReturn(mockCollectionsNames);
        when(mockedStringIterator.next()).thenReturn("a", "b", "c");
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(MONGOCLIENT_PROVIDER_FIELDNAME, mockedMongoClientProvider, servlet.getStrategies().get(MONGO_ROUTE));
        TestUtils.setPrivateField(URI_PROVIDER_FIELDNAME, mockedUriProvider, servlet.getStrategies().get(MONGO_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("OK"));
    }

    @Test
    public void shouldWriteKObecauseWrongDatabase() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(MONGO_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedMongoClient.getDatabase(anyString())).thenReturn(mockedMongoDatabase);
        when(mockedMongoDatabase.listCollectionNames()).thenThrow(MongoCommandException.class);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(MONGOCLIENT_PROVIDER_FIELDNAME, mockedMongoClientProvider, servlet.getStrategies().get(MONGO_ROUTE));
        TestUtils.setPrivateField(URI_PROVIDER_FIELDNAME, mockedUriProvider, servlet.getStrategies().get(MONGO_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKObecauseWrongCredentials() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(MONGO_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedMongoClient.getDatabase(anyString())).thenReturn(mockedMongoDatabase);
        when(mockedMongoDatabase.listCollectionNames()).thenThrow(MongoSecurityException.class);
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(MONGOCLIENT_PROVIDER_FIELDNAME, mockedMongoClientProvider, servlet.getStrategies().get(MONGO_ROUTE));
        TestUtils.setPrivateField(URI_PROVIDER_FIELDNAME, mockedUriProvider, servlet.getStrategies().get(MONGO_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteKObecauseWrongCollections() throws Exception {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_PATH);
        when(request.getPathInfo()).thenReturn(MONGO_ROUTE_PATH);
        when(response.getWriter()).thenReturn(printWriter);
        when(mockedMongoClient.getDatabase(anyString())).thenReturn(mockedMongoDatabase);
        when(mockedMongoDatabase.listCollectionNames()).thenReturn(mockCollectionsNames);
        when(mockedStringIterator.next()).thenReturn("a", "z", "c");
        servlet.init(servletConfiguration);
        TestUtils.setPrivateField(MONGOCLIENT_PROVIDER_FIELDNAME, mockedMongoClientProvider, servlet.getStrategies().get(MONGO_ROUTE));
        TestUtils.setPrivateField(URI_PROVIDER_FIELDNAME, mockedUriProvider, servlet.getStrategies().get(MONGO_ROUTE));
        // WHEN
        try {
            servlet.doGet(request, response);
        } catch (Exception e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

}
