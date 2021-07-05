package io.checkme.surv.handlers;

/*-
 * #%L
 * checkme-http-handlers
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
