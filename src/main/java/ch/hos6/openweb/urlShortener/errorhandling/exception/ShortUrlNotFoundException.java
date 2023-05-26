package ch.hos6.openweb.urlShortener.errorhandling.exception;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }


}
