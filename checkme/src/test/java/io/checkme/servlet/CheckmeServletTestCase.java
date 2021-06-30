package io.checkme.servlet;

import io.checkme.surv.file.CheckSimpleFile;
import org.junit.Test;
import io.checkme.surv.AbstractCheckmeTestCase;
import io.checkme.surv.CheckApp;
import io.checkme.surv.jdbc.CheckSimpleJDBC;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CheckmeServletTestCase extends AbstractCheckmeTestCase {

    private static final String PROPERTIES_FILENAME = "checkme_routes.properties";

    private static final String REPOSITORY_ROUTE = "repository";

    private static final String APPSERVER_ROUTE = "appserver";

    private static final String JDBC_ROUTE = "jdbc";


    @Test
    public void shouldInitServlet() {
        // GIVEN
        CheckmeServlet servlet = new CheckmeServlet();
        // WHEN
        try {
            servlet.init();
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet).isNotNull();
    }

    @Test
    public void shouldContainsKeysInStrategy() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_FILENAME);
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies()).hasSize(3);
        assertThat(servlet.getStrategies()).containsKeys(APPSERVER_ROUTE, JDBC_ROUTE, REPOSITORY_ROUTE);
    }

    @Test
    public void shouldContainsStrategyPaths() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_FILENAME);
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies().get(APPSERVER_ROUTE).getPath()).isEqualTo("/appserver");
        assertThat(servlet.getStrategies().get(JDBC_ROUTE).getPath()).isEqualTo("/jdbc");
        assertThat(servlet.getStrategies().get(REPOSITORY_ROUTE).getPath()).isEqualTo("/repository");
    }

    @Test
    public void shouldContainsStrategyInstances() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet(PROPERTIES_FILENAME);
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies().get(APPSERVER_ROUTE)).isInstanceOf(CheckApp.class);
        assertThat(servlet.getStrategies().get(JDBC_ROUTE)).isInstanceOf(CheckSimpleJDBC.class);
        assertThat(servlet.getStrategies().get(REPOSITORY_ROUTE)).isInstanceOf(CheckSimpleFile.class);
    }

    @Test
    public void shouldNotFindCheckServiceClass() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_errors.properties");
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies()).doesNotContainKey(APPSERVER_ROUTE);
        assertThat(servlet.getStrategies().get(JDBC_ROUTE)).isInstanceOf(CheckSimpleJDBC.class);
    }

    @Test
    public void shouldNotFindRoutesProperties() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_fake.properties");
        try {
            // WHEN
            servlet.init(servletConfiguration);
            failBecauseExceptionWasNotThrown(ServletException.class);
        } catch (ServletException e) {
            // THEN
            assertThat(e).hasMessage("This file does not seem to exist checkme_routes_fake.properties");
        }
    }

    @Test()
    public void shouldValidSomeKeys() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_errors2.properties");
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies()).hasSize(2);
        assertThat(servlet.getStrategies()).containsKeys(APPSERVER_ROUTE, JDBC_ROUTE);
    }

    @Test()
    public void shouldNotInstanciateClasses() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_errors3.properties");
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies()).hasSize(1);
        assertThat(servlet.getStrategies()).containsKeys(APPSERVER_ROUTE);
    }

    @Test()
    public void shouldNotValidInheritance() {
        // GIVEN
        CheckmeServlet servlet = initCheckmeServlet("checkme_routes_errors4.properties");
        // WHEN
        try {
            servlet.init(servletConfiguration);
        } catch (ServletException e) {
            fail(getStackTrace(e));
        }
        // THEN
        assertThat(servlet.getStrategies()).hasSize(1);
        assertThat(servlet.getStrategies()).containsKeys(APPSERVER_ROUTE);
    }
}
