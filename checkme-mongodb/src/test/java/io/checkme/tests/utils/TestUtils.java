package io.checkme.tests.utils;

/*-
 * #%L
 * checkme-mongodb
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

import java.lang.reflect.Field;

public final class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }


    public static void setPrivateField(final String fieldName, final Object valueOfField, final Object recipientObject) {
        try {
            final Field declaredField = recipientObject.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(recipientObject,valueOfField);
        } catch (NoSuchFieldException e) {
            LOGGER.error("Erreur lors de l'appel du set'{}' sur un objet. Le champ n'existe pas",fieldName,e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Erreur lors de l'appel du set'{}' sur un objet. Accès interdit",fieldName,e);
        }
    }


    public static Object getPrivateField(final String fieldName, final Object objectToScan) {
        try {
            final Field declaredField = objectToScan.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(objectToScan);
        } catch (NoSuchFieldException e) {
            LOGGER.error("Erreur lors de l'appel du get'{}' sur un objet. Le champ n'existe pas",fieldName,e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Erreur lors de l'appel du get'{}' sur un objet. Accès interdit",fieldName,e);
        }
        return objectToScan;
    }
}
