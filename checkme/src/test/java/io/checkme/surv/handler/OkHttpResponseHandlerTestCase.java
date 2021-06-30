package io.checkme.surv.handler;

import org.h2.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OkHttpResponseHandlerTestCase {

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;


    @Test
    public void shouldReturnTrue() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(IOUtils.getInputStreamFromString("OK"));
        HttpResponseHandler handler = new OKHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isTrue();
    }


    @Test
    public void shouldReturnFalseBecauseOfContent() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(IOUtils.getInputStreamFromString("KO"));
        HttpResponseHandler handler = new OKHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseBecauseOfStreamError() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenThrow(new IOException());
        HttpResponseHandler handler = new OKHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

}
