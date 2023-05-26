package ch.hos6.openweb.urlShortener.errorhandling.exception;

import java.io.IOException;

public class UrlCreationException extends IOException {
    public UrlCreationException(String message) {
        super(message);
    }

    public UrlCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
