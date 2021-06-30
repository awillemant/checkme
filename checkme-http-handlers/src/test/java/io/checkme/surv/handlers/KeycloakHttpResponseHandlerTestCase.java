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

public class KeycloakHttpResponseHandlerTestCase {

    private static final String KEYCLOAK_TITLE = "<title>Welcome to Keycloak</title>";
    private static final String KEYCLOAK_WRONG_TITLE = "<title>Welcome to not Keycloak</title>";

    @Mock
    private HttpURLConnection mockedHttpUrlConnection;

    @Test
    public void shouldReturnTrue() throws IOException {
        //GIVEN
        InputStream keycloakInputStream = new ByteArrayInputStream(KEYCLOAK_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(keycloakInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new KeycloakHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseBecauseOfContent() throws IOException {
        //GIVEN
        InputStream keycloakWrongInputStream = new ByteArrayInputStream(KEYCLOAK_WRONG_TITLE.getBytes());
        when(mockedHttpUrlConnection.getInputStream()).thenReturn(keycloakWrongInputStream);
        HttpResponseHandler handler = (HttpResponseHandler) new KeycloakHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalseBecauseOfStreamError() throws IOException {
        //GIVEN
        when(mockedHttpUrlConnection.getInputStream()).thenThrow(new IOException());
        HttpResponseHandler handler = (HttpResponseHandler) new KeycloakHttpResponseHandler();
        //WHEN
        boolean result = handler.handle(mockedHttpUrlConnection);
        //THEN
        assertThat(result).isFalse();
    }
}
