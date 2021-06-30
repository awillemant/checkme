package io.checkme.surv.handler;

import io.checkme.utils.HTTPContentProvider;

import java.io.IOException;
import java.net.HttpURLConnection;

public abstract class AbstractSimpleHttpHandler implements HttpResponseHandler {


    public boolean handle(HttpURLConnection connection) {
        try {
            return validation(connection);
        } catch (Exception  e) {
            logErrorMessage(e);
            return false;
        }
    }

    public boolean validation(final HttpURLConnection connection) throws IOException{
        return new HTTPContentProvider().provideHTTPContent(connection).contains(getExpectedContent());
    }

    public abstract void logErrorMessage(Exception e);

    public abstract String getExpectedContent();

}
