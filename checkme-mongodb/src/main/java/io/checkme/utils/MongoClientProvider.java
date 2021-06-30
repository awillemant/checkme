package io.checkme.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientProvider {

    public MongoClient provide(final MongoClientURI uri) {
        return new MongoClient(uri);
    }

}
