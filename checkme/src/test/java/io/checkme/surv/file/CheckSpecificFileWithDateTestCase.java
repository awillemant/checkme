package io.checkme.surv.file;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.checkme.servlet.CheckmeServlet;
import io.checkme.surv.AbstractCheckmeTestCase;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by a527968 on 06/02/2017.
 */
public class CheckSpecificFileWithDateTestCase extends AbstractCheckmeTestCase {

    private static File testFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSpecificFileWithDateTestCase.class);

    @BeforeClass
    public static void initClass() throws IOException {

        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final String filename = "src/test/resources/surveillance." + formater.format(calendar.getTime()) + ".txt";
        testFile = new File(filename);
        final boolean created = testFile.createNewFile();
        if (!created) {
            LOGGER.error("Fichier '{}' non créé !", filename);
        }
    }

    @Test
    public void shouldWriteKOInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_specificFileWithDate.properties");
        when(request.getPathInfo()).thenReturn("/specificFileWithDateKO");
        when(response.getWriter()).thenReturn(printWriter);
        // WHEN
        try {
            servlet.init(servletConfiguration);
            servlet.doGet(request, response);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("KO"));
    }

    @Test
    public void shouldWriteOKInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_specificFileWithDate.properties");
        when(request.getPathInfo()).thenReturn("/specificFileWithDateOK");
        when(response.getWriter()).thenReturn(printWriter);
        // WHEN
        try {
            servlet.init(servletConfiguration);
            servlet.doGet(request, response);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        verify(printWriter, times(1)).write(eq("OK"));
    }

    @AfterClass
    public static void cleanClass() {
        final boolean deleted = testFile.delete();
        if (!deleted) {
            LOGGER.error("Fichier '{}' non supprimé !", testFile.getPath());
        }
    }
}
