package ch.hos6.openweb.urlShortener.domain.repository;

import ch.hos6.openweb.urlShortener.domain.entity.Url;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * This interface defines the repository for managing URLs in the MongoDB database.
 * It extends the MongoRepository interface provided by Spring Data MongoDB.
 * It provides methods for finding URLs by user ID, finding active URLs by their shortened URL,
 * and checking if a URL exists by its shortened URL.
 *
 * @author Toubia Oussama
 */
public interface UrlRepository extends MongoRepository<Url,String> {

    /**
     * Retrieves all URLs associated with the given user ID.
     * @param userId the ID of the user
     * @return an Iterable of URLs associated with the given user ID
     */
    Iterable<Url> findByUserId(@NonNull String userId);

    /**
     * Retrieves the active URL with the given shortened URL.
     * @param shortenedUrl the shortened URL of the URL
     * @return an Optional containing the active URL if it exists, empty otherwise
     */
    Optional<Url> findByShortenedUrlAndActiveIsTrue(@NonNull String shortenedUrl);
    /**
     * Retrieves the URL with the given shortened URL, regardless of its active status.
     * @param shortenedUrl the shortened URL of the URL
     * @return an Optional containing the URL if it exists, empty otherwise
     */
    Optional<Url> findByShortenedUrl(@NonNull String shortenedUrl);

    Optional<Url> findByShortenedUrlAndUserId(@NonNull String shortenedUrl,@NonNull String userId);
    /**
     * Checks if a URL with the given shortened URL exists.
     * @param shortenedUrl the shortened URL of the URL
     * @return true if a URL with the given shortened URL exists, false otherwise
     */
    Boolean existsByShortenedUrl(@NonNull String shortenedUrl);
}
