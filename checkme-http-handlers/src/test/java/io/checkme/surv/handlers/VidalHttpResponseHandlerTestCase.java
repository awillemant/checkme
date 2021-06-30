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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VidalHttpResponseHandlerTestCase {
    private static final String VIDAL_TITLE = "<test><title>Current Data and Application versions</title></test>";
    private static final String VIDAL_WRONG_TITLE = "<test><title>Test Data and Application versions</title></test>";

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;

    @Test
    public void shouldReturnTrue() throws IOException {
        //GIVEN
        InputStream vidalInputStream = new ByteArrayInputStream(VIDAL_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(vidalInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new VidalHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseBecauseOfContent() throws IOException {
        //GIVEN
        InputStream VidalWrongInputStream = new ByteArrayInputStream(VIDAL_WRONG_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(VidalWrongInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new VidalHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseBecauseOfStreamError() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenThrow(new IOException());
        HttpResponseHandler handler = (HttpResponseHandler) new VidalHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }
}
