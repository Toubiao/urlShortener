package ch.hos6.openweb.urlShortener.integration;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.hos6.openweb.urlShortener.config.CacheConfig;

import ch.hos6.openweb.urlShortener.domain.dto.UrlDto;
import ch.hos6.openweb.urlShortener.domain.entity.Url;
import ch.hos6.openweb.urlShortener.domain.repository.UrlRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UrlServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ObjectMapper objectMapper;


//    @Test
    public void testCreateUrl_Success() throws Exception {
        String originalUrl = "https://example.com";
        String userId = "12345";

        MvcResult result = mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((originalUrl))))
                .andExpect(status().isOk())
                .andReturn();

        UrlDto urlDto = objectMapper.readValue(result.getResponse().getContentAsString(), UrlDto.class);
        assertEquals(originalUrl, urlDto.originalUrl());
        assertNotNull(urlDto.shortenedUrl());
        assertNotNull(urlDto.creationDate());
        assertNotNull(urlDto.expirationDate());

        // Verify that the URL is saved in the repository
        Optional<Url> savedUrl = urlRepository.findByShortenedUrlAndUserId(urlDto.shortenedUrl(),userId);
        assertTrue(savedUrl.isPresent());
        assertEquals(originalUrl, savedUrl.get().getOriginalUrl());
        assertEquals(userId, savedUrl.get().getUserId());
        assertEquals(urlDto.shortenedUrl(), savedUrl.get().getShortenedUrl());
        assertNotNull(savedUrl.get().getCreationDate());
        assertNotNull(savedUrl.get().getExpirationDate());
    }

//    @Test
    public void testGetOriginalUrl_ValidShortUrl_Success() throws Exception {
        Url url = createUrlInRepository("https://example.com", "12345", "abc123");

        mockMvc.perform(get("/{shortUrl}", url.getShortenedUrl()))
                .andExpect(status().is3xxRedirection());


    }

//    @Test
    public void testGetOriginalUrl_InvalidShortUrl_NotFound() throws Exception {
        mockMvc.perform(get("/{shortUrl}", "invalid-short-url"))
                .andExpect(status().isNotFound());
    }

//    @Test
    public void testDisableUrl_Success() throws Exception {
        Url url = createUrlInRepository("https://example.com", "12345", "abc123");

        mockMvc.perform(put("/api/v1/urls/{shortUrl}/disable", url.getShortenedUrl()))
                .andExpect(status().isOk());

        // Verify that the URL is disabled in the repository
        Optional<Url> disabledUrl = urlRepository.findById(url.getId());
        assertTrue(disabledUrl.isPresent());
        assertFalse(disabledUrl.get().isActive());

        // Verify that the URL is removed from the cache
        Cache cache = cacheManager.getCache(CacheConfig.URL_CACHE_NAME);
        assertNull(cache.get(url.getShortenedUrl()));
    }

//    @Test
    public void testDeleteUrl_Success() throws Exception {
        Url url = createUrlInRepository("https://example.com", "12345", "abc123");

        mockMvc.perform(delete("/api/v1/urls/{shortUrl}", url.getShortenedUrl()))
                .andExpect(status().isOk());

        // Verify that the URL is deleted from the repository
        Optional<Url> deletedUrl = urlRepository.findById(url.getId());
        assertFalse(deletedUrl.isPresent());
    }

    private Url createUrlInRepository(String originalUrl, String userId, String shortUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setUserId(userId);
        url.setShortenedUrl(shortUrl);
        url.setCreationDate(LocalDateTime.now());
        url.setExpirationDate(LocalDateTime.now().plusMonths(1));

        return urlRepository.save(url);
    }
}
