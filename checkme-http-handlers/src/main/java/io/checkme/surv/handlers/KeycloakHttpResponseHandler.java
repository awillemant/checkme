package io.checkme.surv.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.checkme.surv.handler.AbstractSimpleHttpHandler;

public class KeycloakHttpResponseHandler extends AbstractSimpleHttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakHttpResponseHandler.class);

    @Override
    public void logErrorMessage(Exception e) {
        LOGGER.error("Failed to reach Keycloak :", e);
    }

    @Override
    public String getExpectedContent() {
        return "Welcome to Keycloak";
    }
}
