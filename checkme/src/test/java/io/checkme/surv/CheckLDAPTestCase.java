package io.checkme.surv;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import io.checkme.servlet.CheckmeServlet;

public class CheckLDAPTestCase extends AbstractCheckmeTestCase {

    @Test
    public void shouldWriteKOBecauseOfNoLdapConnection() throws IOException {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_ldap.properties");
        when(request.getPathInfo()).thenReturn("/ldap");
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
    public void shouldCrashBecauseOfBadKey() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_ldap_errors.properties");
        try {
            // WHEN
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            // THEN
            assertThat(e).hasMessage("Impossible to get property : ldap.hostAdress pour la key : hostAdressKey");
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
    }

    @Test
    public void shouldCrashBecauseOfProperties() {
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_ldap_errors2.properties", null);
        try {
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("This file does not seem to exist test.properties");
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
    }

}
