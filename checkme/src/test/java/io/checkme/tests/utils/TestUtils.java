package io.checkme.tests.utils;

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
