package ch.hos6.openweb.urlShortener;

import ch.hos6.openweb.urlShortener.errorhandling.exception.InvalidUrlException;
import ch.hos6.openweb.urlShortener.service.UrlService;
import ch.hos6.openweb.urlShortener.utils.UrlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UrlServiceTest {

    @Autowired
    UrlService urlService;

    @Test
    void testCreateShortUrl_validUrl_returnsShortUrl() {
        String longUrl = "http://google.com";
    }

    @Test
    void testCreateShortUrl_invalidUrl_throwsException() {
        String unreachableURl = "http://notreachable.com";
        assertThrows(InvalidUrlException.class, () -> urlService.createUrl(unreachableURl,"21"));
    }

    @Test
    void testRedirectToOriginalUrl_validShortUrl_redirectsToOriginal() {
        // Test the case where a valid shortened URL is given, expecting the service to redirect to the original URL
    }

    @Test
    void testRedirectToOriginalUrl_expiredShortUrl_throwsException() {
        // Test the case where an expired shortened URL is given, expecting the service to throw an exception
    }

    @Test
    void testRedirectToOriginalUrl_invalidShortUrl_throwsException() {
        // Test the case where an invalid shortened URL is given, expecting the service to throw an exception
    }

    @Test
    void testGetUserUrls_validUserId_returnsUserUrls() {
        // Test the case where a valid user ID is given, expecting the service to return the list of user's URLs
    }

    @Test
    void testGetUserUrls_invalidUserId_throwsException() {
        // Test the case where an invalid user ID is given, expecting the service to throw an exception
    }

    @Test
    void testDeleteUrl_validUrlId_deletesUrl() {
        // Test the case where a valid URL ID is given, expecting the service to delete the URL successfully
    }

    @Test
    void testDeleteUrl_invalidUrlId_throwsException() {
        // Test the case where an invalid URL ID is given, expecting the service to throw an exception
    }

    @Test
    void testDisableUrl_validUrl_disablesUrl() {
        // Test the case where a valid URL ID is given, expecting the service to disable the URL successfully
    }

    @Test
    void testDisableUrl_invalidUrl_throwsException() {
        // Test the case where an invalid URL ID is given, expecting the service to throw an exception
    }

    @Test
    void testEnableUrl_disabledUrl_enablesUrl() {
        // Test the case where a disabled URL ID is given, expecting the service to enable the URL successfully
    }

    @Test
    void testEnableUrl_invalidUrl_throwsException() {
        // Test the case where an invalid URL ID is given, expecting the service to throw an exception
    }
}
