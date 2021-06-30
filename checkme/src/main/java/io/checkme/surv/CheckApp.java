package io.checkme.surv;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckApp extends AbstractCheckme {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckApp.class);

    public CheckApp(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckApp with path:{}", path);
    }

    @Override
    public boolean isOk() {
        LOGGER.debug("Executing CheckApp");
        return true;
    }
}
