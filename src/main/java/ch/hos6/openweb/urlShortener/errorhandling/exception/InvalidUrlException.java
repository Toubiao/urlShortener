package ch.hos6.openweb.urlShortener.errorhandling.exception;

import java.io.IOException;

public class InvalidUrlException extends IOException {
    public InvalidUrlException(String message) {
        super(message);
    }

    public InvalidUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
