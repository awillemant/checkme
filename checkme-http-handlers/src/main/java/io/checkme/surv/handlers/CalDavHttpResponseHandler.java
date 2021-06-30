package io.checkme.surv.handlers;

import io.checkme.surv.handler.AbstractSimpleHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.checkme.surv.handler.AbstractSimpleHttpHandler;

public class CalDavHttpResponseHandler extends AbstractSimpleHttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalDavHttpResponseHandler.class);

    @Override
    public void logErrorMessage(Exception e) {
        LOGGER.error("Failed to reach Caldav :", e);
    }

    @Override
    public String getExpectedContent() {
        return "Web interface for Radicale";
    }
}
