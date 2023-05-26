package ch.hos6.openweb.urlShortener.domain.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Url is a representation of a shortened URL in the system.
 * It is stored in the 'urls' collection in MongoDB.
 *
 * @author Toubia Oussama
 */
@Data
@Document(collection = "urls")
public class Url {
    /**
     * Unique identifier for the Url.
     * It is the primary key in the 'urls' collection.
     */
    @Id
    private String id;

    /**
     * The identifier of the user who created this Url.
     */
    private String userId;

    /**
     * The original URL that was shortened.
     */
    @NotBlank
    private String originalUrl;

    /**
     * The shortened version of the original URL.
     */
    @NotBlank
    @Indexed(unique = true)
    private String shortenedUrl;

    /**
     * The date and time when this Url was created.
     */
    @Indexed(expireAfterSeconds = 30*24*3600)
    private LocalDateTime creationDate;

    /**
     * The date and time when this Url will expire.
     * After this time, the Url will be automatically removed from the collection due to the TTL index.
     */
    private LocalDateTime expirationDate;

    /**
     * Indicates whether this Url is active.
     * If true, the Url can be used for redirection. If false, it cannot.
     */
    private boolean active = true;


}