package io.checkme.surv;

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

import io.checkme.utils.MongoClientProvider;
import io.checkme.utils.UriProvider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

public class CheckMongoDB extends AbstractCheckme {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMongoDB.class);

    private static final String URI_MONGO_PARAM_KEY = "url";
    private static final String DATABASE_MONGO_PARAM_KEY = "database";
    private static final String MONGO_COLLECTIONS_NAMES_JOINED = "collections";

    private UriProvider uriProvider;

    private MongoClientProvider mongoClientProvider;

    public CheckMongoDB(String path, ServletContext servletContext) {
        super(path, servletContext);
        uriProvider = new UriProvider();
        mongoClientProvider = new MongoClientProvider();
        LOGGER.debug("Building CheckhMongoDB with path:{}", path);
    }

    private MongoClientURI getURI() {
        return uriProvider.provideUri(getMandatoryConfiguration(URI_MONGO_PARAM_KEY));
    }

    @Override
    protected boolean isOk() {
        LOGGER.debug("Executing CheckMongoDB");
        return connect();
    }

    private boolean connect() {
        try (MongoClient mongoClient = mongoClientProvider.provide(getURI())) {
            String databaseName = getMandatoryConfiguration(DATABASE_MONGO_PARAM_KEY);
            String collectionsStringProperties = getMandatoryConfiguration(MONGO_COLLECTIONS_NAMES_JOINED);

            MongoIterable<String> listCollectionsNames = mongoClient.getDatabase(databaseName).listCollectionNames();

            String collectionsNamesJoined = String.join(";", listCollectionsNames);

            List<String> actualCollections=Arrays.asList(collectionsNamesJoined.split(";"));

            List<String> expectedCollections = Arrays.asList(collectionsStringProperties.split(";"));

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Joined collections names : {}", String.join(";", actualCollections));
            }

            return actualCollections.containsAll(expectedCollections);

        } catch (Exception e) {
            LOGGER.error("Failed to access MongoDB", e);
            return false;
        }
    }

}
