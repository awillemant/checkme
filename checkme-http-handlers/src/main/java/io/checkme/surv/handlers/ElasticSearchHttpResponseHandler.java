package io.checkme.surv.handlers;

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
