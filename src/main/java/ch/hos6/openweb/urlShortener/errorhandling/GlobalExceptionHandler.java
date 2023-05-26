package ch.hos6.openweb.urlShortener.errorhandling;

import ch.hos6.openweb.urlShortener.errorhandling.exception.ShortUrlNotFoundException;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UrlCreationException;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UsernameAlreadyTakenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

/**
 * GlobalExceptionHandler is a centralized handler that handles all exceptions thrown across the whole application.
 * It uses the @ControllerAdvice annotation to handle exceptions globally across all controller classes.
 * It also uses the @ExceptionHandler annotation to handle specific exceptions.
 *
 * @author Toubia Oussama
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method handles all exceptions that do not have a specific handler.
     *
     * @param e the exception that was thrown.
     * @return a ResponseEntity with a message describing the exception and an INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Other exception caught: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method handles AuthenticationException exceptions.
     *
     * @param ex the AuthenticationException that was thrown.
     * @return a ResponseEntity with a message indicating invalid username or password and an UNAUTHORIZED status.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleBadCredentialsException(AuthenticationException ex) {
       log.error("BadCredentialsException caught: " + ex.getMessage());
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method handles ShortUrlNotFoundException exceptions.
     *
     * @param ex the ShortUrlNotFoundException that was thrown.
     * @return a ResponseEntity with a message from the exception and a NOT_FOUND status.
     */
    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<String> handleShortUrnNotFoundException(ShortUrlNotFoundException ex) {
        log.error("ShortUrlNotFoundException caught: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * This method handles UsernameAlreadyTakenException exceptions.
     *
     * @param ex the UsernameAlreadyTakenException that was thrown.
     * @return a ResponseEntity with a message from the exception and a NOT_ACCEPTABLE status.
     */
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<String> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        log.error("UsernameAlreadyTakenException caught: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * This method handles UrlCreationException exceptions.
     *
     * @param ex the UrlCreationException that was thrown.
     * @return a ResponseEntity with a message from the exception and an INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(UrlCreationException.class)
    public ResponseEntity<String> handleUrlCreationException(UrlCreationException ex) {
        log.error("UrlCreationException caught: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
