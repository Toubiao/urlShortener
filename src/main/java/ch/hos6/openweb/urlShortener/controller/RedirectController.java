package ch.hos6.openweb.urlShortener.controller;

import ch.hos6.openweb.urlShortener.errorhandling.exception.ShortUrlNotFoundException;
import ch.hos6.openweb.urlShortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * This controller is responsible for redirecting short URLs to their original URLs.
 *
 * @author Toubia Oussama
 */
@Slf4j
@RestController
@CrossOrigin(origins="*", maxAge=3600)
@Tag(name = "Redirect", description = "Endpoint for redirecting short URLs to their original URLs")
public class RedirectController {


    /**
     * The service that provides operations related to URLs.
     */
    private final UrlService urlService;

    /**
     * Constructs a new instance of RedirectController with the given UrlService.
     *
     * @param urlService The service that provides operations related to URLs.
     */
    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Fetches the original URL corresponding to the given short URL and redirects to it.
     *
     * @param shortUrl The short URL that should be redirected to its original URL.
     * @param response The HttpServletResponse to which the redirect should be written.
     * @return A ResponseEntity indicating the outcome of the operation.
     */
    @GetMapping("/{shortUrl}")
    @Operation(summary = "Redirect to original URL", description = "Fetches the original URL corresponding to the given short URL and redirects to it")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortUrl);
            response.sendRedirect(originalUrl);
            return new ResponseEntity<>("Redirecting to original URL.", HttpStatus.MOVED_PERMANENTLY);
        }  catch (IOException e) {
            log.error("Error while redirecting to original URL: {}", shortUrl, e);
            return new ResponseEntity<>("Error while redirecting to original URL.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
