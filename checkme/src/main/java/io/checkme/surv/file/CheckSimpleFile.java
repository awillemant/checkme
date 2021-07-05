package io.checkme.surv.file;

/*-
 * #%L
 * checkme
 * %%
 * Copyright (C) 2013 - 2021 Apa
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
