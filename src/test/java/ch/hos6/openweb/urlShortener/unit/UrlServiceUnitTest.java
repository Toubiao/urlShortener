package ch.hos6.openweb.urlShortener.unit;

import ch.hos6.openweb.urlShortener.domain.entity.Url;
import ch.hos6.openweb.urlShortener.domain.repository.UrlRepository;
import ch.hos6.openweb.urlShortener.errorhandling.exception.InvalidUrlException;
import ch.hos6.openweb.urlShortener.errorhandling.exception.ShortUrlNotFoundException;
import ch.hos6.openweb.urlShortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UrlServiceUnitTest {
    private UrlRepository urlRepository;
    private CacheManager cacheManager;
    private UrlService urlService;

    @BeforeEach
    public void setUp() {
        urlRepository = Mockito.mock(UrlRepository.class);
        cacheManager = Mockito.mock(CacheManager.class);
        urlService = new UrlService(urlRepository, cacheManager);
    }

    @Test
    public void testCreateUrl_ValidUrl_Success() throws IOException {
        String originalUrl = "https://example.com";
        String userId = "12345";
        String shortUrl = "abc123";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setUserId(userId);
        url.setShortenedUrl(shortUrl);
        url.setCreationDate(LocalDateTime.now());
        url.setExpirationDate(LocalDateTime.now().plusMonths(1));

        when(urlRepository.existsByShortenedUrl(anyString())).thenReturn(false);
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        Url resultUrl = urlService.createUrl(originalUrl, userId);

        assertNotNull(resultUrl);
        assertEquals(originalUrl, resultUrl.getOriginalUrl());
        assertEquals(userId, resultUrl.getUserId());
        assertEquals(shortUrl, resultUrl.getShortenedUrl());

        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    public void testCreateUrl_InvalidUrl_ThrowsInvalidUrlException() throws IOException {
        String originalUrl = "invalidurl";
        String userId = "12345";

        assertThrows(InvalidUrlException.class, () -> urlService.createUrl(originalUrl, userId));

        verify(urlRepository, never()).existsByShortenedUrl(anyString());
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    public void testCreateUrl_DuplicateShortUrl_ReturnsUrl() throws IOException {
        String originalUrl = "https://example.com";
        String userId = "12345";
        String shortUrl = "abc123";

        when(urlRepository.existsByShortenedUrl(shortUrl)).thenReturn(false, true);
        when(urlRepository.save(any(Url.class))).thenReturn(new Url());

        Url createdUrl = urlService.createUrl(originalUrl, userId);

        assertNotNull(createdUrl);
        // Add assertions for other properties of the created URL

        verify(urlRepository, atLeastOnce()).save(any(Url.class));
    }

    @Test
    public void testGetOriginalUrl_ValidShortUrl_Success() throws ShortUrlNotFoundException {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";

        Url url = new Url();
        url.setShortenedUrl(shortUrl);
        url.setOriginalUrl(originalUrl);
        url.setActive(true);

        when(urlRepository.findByShortenedUrlAndActiveIsTrue(anyString())).thenReturn(Optional.of(url));

        String resultUrl = urlService.getOriginalUrl(shortUrl);

        assertEquals(originalUrl, resultUrl);
    }

    @Test
    public void testGetOriginalUrl_InvalidShortUrl_ThrowsShortUrlNotFoundException() {
        String shortUrl = "invalidshorturl";

        when(urlRepository.findByShortenedUrlAndActiveIsTrue(anyString())).thenReturn(Optional.empty());

        assertThrows(ShortUrlNotFoundException.class, () -> urlService.getOriginalUrl(shortUrl));
    }

    @Test
    public void testGetUserUrls_ValidUserId_Success() {
        String userId = "12345";

        Url url1 = new Url();
        url1.setUserId(userId);
        url1.setShortenedUrl("shortUrl1");
        url1.setOriginalUrl("https://example.com/1");
        url1.setActive(true);

        Url url2 = new Url();
        url2.setUserId(userId);
        url2.setShortenedUrl("shortUrl2");
        url2.setOriginalUrl("https://example.com/2");
        url2.setActive(true);

        when(urlRepository.findByUserId(anyString())).thenReturn(Collections.singletonList(url1));

        Iterable<Url> resultUrls = urlService.getUserUrls(userId);

        assertEquals(1, resultUrls.spliterator().getExactSizeIfKnown());
        assertEquals(url1, resultUrls.iterator().next());
    }

    @Test
    public void testDeleteUrl_Success() throws ShortUrlNotFoundException {
        String shortUrl = "abc123";
        String userId = "user12345";

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.of(new Url()));

        urlService.deleteUrl(shortUrl,userId);

        verify(urlRepository, times(1)).delete(any(Url.class));
    }

    @Test
    public void testDeleteUrl_NotFound() {
        String shortUrl = "abc123";
        String userId = "user12345";

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.empty());

        assertThrows(ShortUrlNotFoundException.class, () -> urlService.deleteUrl(shortUrl,userId));
    }

    @Test
    public void testDisableUrl_Success() throws ShortUrlNotFoundException {
        String shortUrl = "abc123";
        String userId = "user12345";
        Cache cache = Mockito.mock(Cache.class);
        Url url = new Url();
        url.setShortenedUrl(shortUrl);
        url.setActive(true);

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.of(url));
        when(cacheManager.getCache(anyString())).thenReturn(cache);

        urlService.disableUrl(shortUrl,userId);

        assertFalse(url.isActive());
        verify(urlRepository, times(1)).save(any(Url.class));

    }

    @Test
    public void testDisableUrl_NotFound() {
        String shortUrl = "abc123";
        String userId = "user12345";

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.empty());

        assertThrows(ShortUrlNotFoundException.class, () -> urlService.disableUrl(shortUrl,userId));
    }

    @Test
    public void testEnableUrl_Success() throws ShortUrlNotFoundException {
        String shortUrl = "abc123";
        String userId = "user12345";
        Cache cache = Mockito.mock(Cache.class);
        Url url = new Url();
        url.setShortenedUrl(shortUrl);
        url.setActive(false);

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.of(url));
        when(cacheManager.getCache(anyString())).thenReturn(cache);

        urlService.enableUrl(shortUrl,userId);

        assertTrue(url.isActive());
        verify(urlRepository, times(1)).save(any(Url.class));
        verify(cache, times(1)).put(anyString(), any());
    }

    @Test
    public void testEnableUrl_NotFound() {
        String shortUrl = "abc123";
        String userId = "user12345";

        when(urlRepository.findByShortenedUrlAndUserId(anyString(),anyString())).thenReturn(Optional.empty());

        assertThrows(ShortUrlNotFoundException.class, () -> urlService.enableUrl(shortUrl,userId));
    }
}
