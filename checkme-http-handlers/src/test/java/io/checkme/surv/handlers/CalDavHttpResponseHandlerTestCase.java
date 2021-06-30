package io.checkme.surv.handlers;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.checkme.surv.handler.HttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class CalDavHttpResponseHandlerTestCase {

    private static final String CALDAV_TITLE = "<title>Web interface for Radicale</title>";
    private static final String CALDAV_WRONG_TITLE = "<title>Test interface for Radicale</title>";

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;

    @Test
    public void shouldReturnTrue() throws IOException {
        //GIVEN
        InputStream caldavInputStream = new ByteArrayInputStream(CALDAV_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(caldavInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new CalDavHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseBecauseOfContent() throws IOException {
        //GIVEN
        InputStream caldavWrongInputStream = new ByteArrayInputStream(CALDAV_WRONG_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(caldavWrongInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new CalDavHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseBecauseOfStreamError() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenThrow(new IOException());
        HttpResponseHandler handler = (HttpResponseHandler) new CalDavHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }
}
