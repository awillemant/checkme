package io.checkme.surv.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OKHttpResponseHandler extends AbstractSimpleHttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OKHttpResponseHandler.class);


    @Override
    public void logErrorMessage(Exception e) {
        LOGGER.error("Failed to reach service :", e);
    }

    @Override
    public String getExpectedContent() {
        return "OK";
    }
}
