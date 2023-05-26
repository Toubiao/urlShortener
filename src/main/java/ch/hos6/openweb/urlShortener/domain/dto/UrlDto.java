package ch.hos6.openweb.urlShortener.domain.dto;

import java.time.LocalDateTime;

public record UrlDto(String originalUrl,
                     String shortenedUrl,
                     LocalDateTime creationDate,
                     LocalDateTime expirationDate,
                     boolean active) {
}
