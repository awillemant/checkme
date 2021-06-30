package io.checkme.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlProvider {

    public URL provideUrl(final String url) throws MalformedURLException {
        return new URL(url);
    }
}
