package io.checkme.surv.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.checkme.surv.handler.AbstractSimpleHttpHandler;

public class WSO2HttpResponseHandler extends AbstractSimpleHttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WSO2HttpResponseHandler.class);

    @Override
    public void logErrorMessage(Exception e) {
        LOGGER.error("Failed to reach WSO2 AM :", e);
    }

    @Override
    public String getExpectedContent() {
        return "Welcome to APIM";
    }
}
