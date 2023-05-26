package ch.hos6.openweb.urlShortener.errorhandling.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

}
