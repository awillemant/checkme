package io.checkme.surv.jdbc;

import org.h2.util.IOUtils;
import org.junit.Test;
import io.checkme.servlet.CheckmeServlet;
import io.checkme.surv.AbstractCheckmeTestCase;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by a527968 on 07/02/2017.
 */
public class CheckSpecificRequestJDBCTestCase extends AbstractCheckmeTestCase {


    @Test
    public void shouldWriteOKInResponse() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_route_specificRequestJDBC.properties", IOUtils.getInputStreamFromString(""));
        when(request.getPathInfo()).thenReturn("/sqlRequest");
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

}
