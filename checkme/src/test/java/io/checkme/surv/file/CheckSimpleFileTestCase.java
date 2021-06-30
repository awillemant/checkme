package io.checkme.surv.file;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import io.checkme.servlet.CheckmeServlet;
import io.checkme.surv.AbstractCheckmeTestCase;

public class CheckSimpleFileTestCase extends AbstractCheckmeTestCase {

    @Test
    public void shouldWriteOKInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_repository.properties");
        when(request.getPathInfo()).thenReturn("/existingRepository");
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

    @Test
    public void shouldWriteKOInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_repository.properties");
        when(request.getPathInfo()).thenReturn("/notExistingRepository");
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
}
