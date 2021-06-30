package io.checkme.surv;

import org.h2.util.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import io.checkme.servlet.CheckmeServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCheckmeTestCase {

    @Mock
    protected ServletConfig servletConfiguration;

    @Mock
    protected ServletContext servletContext;

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected PrintWriter printWriter;

    public CheckmeServlet initCheckmeServlet(String routesPath) {
        return initCheckmeServlet(routesPath, IOUtils.getInputStreamFromString(""));
    }

    protected CheckmeServlet initCheckmeServlet(String routesPath, InputStream servletContextMockGetResourceAsStream) {
        CheckmeServlet servlet = new CheckmeServlet();
        when(servletConfiguration.getInitParameter(eq("routesProperties"))).thenReturn(routesPath);
        when(servletConfiguration.getServletContext()).thenReturn(servletContext);
        return servlet;
    }

}
