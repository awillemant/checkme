package io.checkme.surv.file;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckSimpleFile extends AbstractCheckFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSimpleFile.class);

    private static final String FILE_TO_TEST = "fileToTest";

    public CheckSimpleFile(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckSimpleFile with path:{}", path);
    }

    @Override
    String getFiletoSurv() {
        LOGGER.debug("Executing CheckSimpleFile");
        return getMandatoryConfiguration(FILE_TO_TEST);
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

}
