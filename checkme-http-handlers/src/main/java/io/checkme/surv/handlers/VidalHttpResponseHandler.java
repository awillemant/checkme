package io.checkme.surv.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.checkme.surv.handler.AbstractSimpleHttpHandler;

public class VidalHttpResponseHandler extends AbstractSimpleHttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VidalHttpResponseHandler.class);

    @Override
    public void logErrorMessage(Exception e) {
        LOGGER.error("Failed to reach Vidal :", e);
    }

    @Override
    public String getExpectedContent() {
        return "Current Data and Application versions";
    }
}
