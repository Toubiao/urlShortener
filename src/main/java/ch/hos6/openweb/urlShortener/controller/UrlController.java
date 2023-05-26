package ch.hos6.openweb.urlShortener.controller;

import ch.hos6.openweb.urlShortener.domain.dto.UrlDto;
import ch.hos6.openweb.urlShortener.domain.entity.Url;
import ch.hos6.openweb.urlShortener.errorhandling.exception.UrlCreationException;
import ch.hos6.openweb.urlShortener.mapper.UrlMapper;
import ch.hos6.openweb.urlShortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * This class acts as a REST controller for handling URL related requests.
 * It provides endpoints for creating a short URL, retrieving user URLs, deleting a URL, disabling a URL and enabling a URL.
 *
 * @author Toubia Oussama
 */
@Slf4j
@RestController
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/api/v1/urls")
@Tag(name = "URL", description = "Endpoints for managing URLs")
public class UrlController {

    private final UrlService urlService;
    private final UrlMapper urlMapper;

    /**
     * Constructs a new UrlController with the given UrlService and UrlMapper.
     * @param urlService the service for managing URLs
     * @param urlMapper the mapper for converting between URLs and their corresponding DTOs
     */
    public UrlController(UrlService urlService, UrlMapper urlMapper) {
        this.urlService = urlService;
        this.urlMapper = urlMapper;
    }

    /**
     * Creates a new short URL from the given original URL and returns its corresponding DTO.
     * @param originalUrl the original URL to be shortened
     * @param jwtAuthenticationToken the JWT token for authenticating the user
     * @return the DTO of the created short URL
     */
    @PostMapping
    @Operation(summary = "Create short URL", description = "Creates a new short URL from the given original URL")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<UrlDto> createShortUrl(@RequestBody String originalUrl, JwtAuthenticationToken jwtAuthenticationToken) throws UrlCreationException {
        Url url = null;
        String userId = getUserIdFromJwtAuth(jwtAuthenticationToken);
        try {
            url = urlService.createUrl(originalUrl,userId);
        } catch (IOException e) {
            log.error("Unable to create URL {} ", originalUrl, e);
            throw new UrlCreationException("Unable to create URL " + originalUrl,e);
        }
        return ResponseEntity.ok(urlMapper.urlToDto(url));
    }

    /**
     * Retrieves all URLs of the authenticated user and returns their corresponding DTOs.
     * @param jwtAuthenticationToken the JWT token for authenticating the user
     * @return the DTOs of the user's URLs
     */
    @GetMapping
    @Operation(summary = "Get user URLs", description = "Retrieves all URLs of the authenticated user")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<Iterable<UrlDto>> getUserUrls(JwtAuthenticationToken jwtAuthenticationToken) {
        String userId = getUserIdFromJwtAuth(jwtAuthenticationToken);
        Iterable<Url> urls = urlService.getUserUrls(userId);
        return ResponseEntity.ok(urlMapper.urlsToDtos(urls));
    }

    /**
     * Deletes the URL with the given short URL.
     * @param shortUrl the short URL of the URL to be deleted
     */
    @DeleteMapping("/{shortUrl}")
    @Operation(summary = "Delete URL", description = "Deletes the URL with the given short URL")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortUrl,JwtAuthenticationToken jwtAuthenticationToken) {
        String userId = getUserIdFromJwtAuth(jwtAuthenticationToken);
        urlService.deleteUrl(shortUrl,userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Disables the URL with the given short URL.
     * @param shortUrl the short URL of the URL to be disabled
     */
    @PutMapping("/{shortUrl}/disable")
    @Operation(summary = "Disable URL", description = "Disables the URL with the given short URL")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<Void> disableUrl(@PathVariable String shortUrl,JwtAuthenticationToken jwtAuthenticationToken) {
        String userId = getUserIdFromJwtAuth(jwtAuthenticationToken);
         urlService.disableUrl(shortUrl,userId);
         return ResponseEntity.noContent().build();
    }

    /**
     * Enables the URL with the given short URL.
     * @param shortUrl the short URL of the URL to be enabled
     */
    @PutMapping("/{shortUrl}/enable")
    @Operation(summary = "Enable URL", description = "Enables the URL with the given short URL")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<Void> enableUrl(@PathVariable String shortUrl,JwtAuthenticationToken jwtAuthenticationToken) {
        String userId = getUserIdFromJwtAuth(jwtAuthenticationToken);
        urlService.enableUrl(shortUrl,userId);
        return ResponseEntity.noContent().build();
    }

    private String getUserIdFromJwtAuth(JwtAuthenticationToken jwtAuthenticationToken){
        return jwtAuthenticationToken.getTokenAttributes().get("userId").toString();
    }
}
