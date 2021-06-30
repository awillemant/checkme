package io.checkme.utils;

import com.mongodb.MongoClientURI;

public class UriProvider {
    public MongoClientURI provideUri(final String uri){
        return new MongoClientURI(uri);
    }
}
