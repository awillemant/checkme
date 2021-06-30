package io.checkme.surv.handler;

import java.net.HttpURLConnection;

public interface HttpResponseHandler {
    boolean handle(HttpURLConnection connection);
}
