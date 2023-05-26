package ch.hos6.openweb.urlShortener.service;

import ch.hos6.openweb.urlShortener.aspect.RecordTime;
import ch.hos6.openweb.urlShortener.config.CacheConfig;
import ch.hos6.openweb.urlShortener.domain.entity.Url;
import ch.hos6.openweb.urlShortener.domain.repository.UrlRepository;
import ch.hos6.openweb.urlShortener.errorhandling.exception.InvalidUrlException;
import ch.hos6.openweb.urlShortener.errorhandling.exception.ShortUrlNotFoundException;
import ch.hos6.openweb.urlShortener.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * UrlService provides services related to the Url entity.
 * It includes functionality to create, retrieve, disable, and delete Urls.
 *
 * @author Toubia Oussama
 */
@Slf4j
@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final CacheManager cacheManager;

    /**
     * Constructor for the UrlService.
     *
     * @param urlRepository The repository for URL entities.
     * @param cacheManager  The manager for caching URL data.
     */
    public UrlService(UrlRepository urlRepository, CacheManager cacheManager) {
        this.urlRepository = urlRepository;
        this.cacheManager = cacheManager;
    }


    /**
     * Creates a new URL entity and saves it to the repository.
     *
     * @param originalUrl The original URL to be shortened.
     * @param userId      The user ID associated with the URL.
     * @return The saved URL entity.
     */
    @Transactional
    @RecordTime
    public Url createUrl(String originalUrl, String userId) throws IOException {
        try {
            UrlUtils.isValidUrl(originalUrl);
            String shortUrl = generateUniqueShortUrl(originalUrl);
            return saveUrl(originalUrl, userId, shortUrl);
        } catch (IOException e) {
            log.error("Invalid URL {}", originalUrl);
            throw new InvalidUrlException("Invalid URL", e);
        }
    }


    /**
     * Retrieves the original URL from a shortened URL.
     *
     * @param shortUrl the shortened URL
     * @return the original URL
     * @throws ShortUrlNotFoundException if the short URL is invalid or inactive
     */
    @Cacheable(value = CacheConfig.URL_CACHE_NAME, key = "#shortUrl")
    public String getOriginalUrl(String shortUrl) throws ShortUrlNotFoundException {
        return findByShortenedUrlAndActive(shortUrl)
                .orElseThrow(() -> new ShortUrlNotFoundException("Invalid or inactive short URL"))
                .getOriginalUrl();
    }

    /**
     * Retrieves all Urls for a user.
     *
     * @param userId the ID of the user
     * @return an iterable of Urls for the user
     */
    public Iterable<Url> getUserUrls(String userId) {
        return urlRepository.findByUserId(userId);
    }

    /**
     * Deletes a Url.
     *
     * @param shortUrl the shortened URL to delete
     * @param userId The user id of the URL to be
     * @throws ShortUrlNotFoundException if the short URL is invalid
     */
    @Transactional
    @CacheEvict(value = CacheConfig.URL_CACHE_NAME, key = "#shortUrl")
    public void deleteUrl(String shortUrl,String userId) throws ShortUrlNotFoundException {
        Url url = findByShortenedUrlAnbUserId(shortUrl,userId)
                .orElseThrow(() -> new ShortUrlNotFoundException("Invalid short URL"));
        urlRepository.delete(url);
    }

    /**
     * Disables a Url.
     *
     * @param shortUrl the shortened URL to disable
     * @param userId The user id of the URL to disable
     * @throws ShortUrlNotFoundException if the short URL is invalid
     */
    @Transactional
    @CacheEvict(value = CacheConfig.URL_CACHE_NAME, key = "#shortUrl")
    public void disableUrl(String shortUrl,String userId) throws ShortUrlNotFoundException {
        updateUrlStatus(shortUrl, false,userId);
    }


    /**
     * Enables a Url.
     *
     * @param shortUrl the shortened URL to enable
     * @param userId The user id of the URL to enable
     * @throws ShortUrlNotFoundException if the short URL is invalid
     */
    public void enableUrl(String shortUrl,String userId) throws ShortUrlNotFoundException {
        updateUrlStatus(shortUrl, true,userId);
    }

    /**
     * Generates a unique short URL for the provided original URL.
     * It uses UrlUtils to shorten the URL and checks the URL repository to ensure uniqueness.
     * If a generated short URL already exists, it generates a new one until a unique short URL is found.
     *
     * @param originalUrl The original URL to be shortened.
     * @return A unique short URL.
     */
    private String generateUniqueShortUrl(String originalUrl) {
        String shortUrl = UrlUtils.shortenUrl(originalUrl);

        // Keep generating new short URLs until we find one that doesn't exist yet
        while (urlRepository.existsByShortenedUrl(shortUrl)) {
            shortUrl = UrlUtils.shortenUrl(originalUrl + Math.random());
        }

        return shortUrl;
    }

    /**
     * Saves the URL to the URL repository.
     * The method creates a new URL object, sets its properties, and saves it to the URL repository.
     *
     * @param originalUrl The original URL.
     * @param userId The ID of the user who created the URL.
     * @param shortUrl The shortened version of the original URL.
     * @return The saved URL object.
     */
    private Url saveUrl(String originalUrl, String userId, String shortUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setUserId(userId);
        url.setShortenedUrl(shortUrl);
        url.setCreationDate(LocalDateTime.now());
        url.setExpirationDate(LocalDateTime.now().plusMonths(1));

        return urlRepository.save(url);
    }

    /**
     * Updates the status of the URL in the URL repository.
     * The method finds the URL in the repository, updates its status, and saves the updated URL.
     * If the URL is set to active, it is also added to the cache.
     *
     * @param shortUrl The short URL of the URL to be updated.
     * @param status The new status of the URL.
     * @param userId The user id of the URL to be updated.
     */
    private void updateUrlStatus(String shortUrl, boolean status,String userId) {
        Url url = findByShortenedUrlAnbUserId(shortUrl,userId)
                .orElseThrow(() -> new ShortUrlNotFoundException("Invalid short URL"));

        url.setActive(status);
        urlRepository.save(url);

        if (status) {
            Objects.requireNonNull(cacheManager.getCache(CacheConfig.URL_CACHE_NAME)).put(shortUrl, url.getOriginalUrl());
        }
    }

    /**
     * Finds a URL in the URL repository by its short URL.
     *
     * @param shortUrl The short URL of the URL to be found.
     * @param userId The user id of the URL to be found.
     * @return An Optional containing the found URL if it exists, or an empty Optional if it doesn't.
     */
    private Optional<Url> findByShortenedUrlAnbUserId(String shortUrl, String userId) {
        return urlRepository.findByShortenedUrlAndUserId(shortUrl,userId);
    }

    /**
     * Finds an active URL in the URL repository by its short URL.
     *
     * @param shortUrl The short URL of the URL to be found.
     * @return An Optional containing the found URL if it exists and is active, or an empty Optional if it doesn't exist or is not active.
     */
    private Optional<Url> findByShortenedUrlAndActive(String shortUrl) {
        return urlRepository.findByShortenedUrlAndActiveIsTrue(shortUrl);
    }
}
