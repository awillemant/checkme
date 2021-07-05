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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by a527968 on 06/02/2017.
 */
public class CheckSpecificFileWithDate extends AbstractCheckFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSpecificFileWithDate.class);

    private static final String TEMPLATE_FILE_TO_TEST = "templateFileToTest";
    private static final String DELAY_VALUE = "delayValue";
    private static final String DELAY_TYPE = "delayType";
    private static final String DATE_FORMAT = "dateFormat";

    public CheckSpecificFileWithDate(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckSpecificFileWithDate with path:{}", path);
    }

    @Override
    String getFiletoSurv() {
        String templateFileToTest = getMandatoryConfiguration(TEMPLATE_FILE_TO_TEST);
        return templateFileToTest.replace("%DATE%", getFileDate());
    }

    private String getFileDate() {
        SimpleDateFormat formater = new SimpleDateFormat(getMandatoryConfiguration(DATE_FORMAT));
        Calendar calendar = Calendar.getInstance();
        calendar.add(getDelayType(), getDelayValue());

        String fileDate = formater.format(calendar.getTime());
        getLogger().debug("File date is {}", fileDate);
        return fileDate;
    }


    @Override
    Logger getLogger() {
        return LOGGER;
    }

    private int getDelayType() {
        switch (getMandatoryConfiguration(DELAY_TYPE)) {
            case "DAY":
                return Calendar.DAY_OF_MONTH;
            case "HOUR":
                return Calendar.HOUR_OF_DAY;
            case "MINUTE":
                return Calendar.MINUTE;
            case "SECOND":
                return Calendar.SECOND;
            default:
                return Calendar.SECOND;
        }
    }

    private int getDelayValue() {
        String delayValue = getMandatoryConfiguration(DELAY_VALUE);
        if (delayValue != null) {
            return Integer.parseInt(delayValue);
        }
        return 0;
    }

}
