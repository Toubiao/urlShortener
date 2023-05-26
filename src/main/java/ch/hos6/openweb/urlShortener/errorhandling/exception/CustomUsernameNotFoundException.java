package ch.hos6.openweb.urlShortener.errorhandling.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomUsernameNotFoundException extends AuthenticationException {

    public CustomUsernameNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomUsernameNotFoundException(String msg) {
        super(msg);
    }
}
