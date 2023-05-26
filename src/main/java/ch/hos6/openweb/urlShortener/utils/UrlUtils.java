package ch.hos6.openweb.urlShortener.utils;

import ch.hos6.openweb.urlShortener.errorhandling.exception.InvalidUrlException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
public class UrlUtils {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 OPR/96.0.0.0";


    /**
     * Validates the provided URL by checking if it matches a valid URL format and then attempting to open a connection to it.
     *
     * @param url the URL to validate
     * @throws InvalidUrlException if the URL is not valid or a connection cannot be opened
     */
    public static void isValidUrl(String url) throws InvalidUrlException {
        URL longUrl;
        HttpURLConnection connection = null;
        try {
            longUrl = new URL(url);
            connection = (HttpURLConnection) longUrl.openConnection();
            //add user agent to prevent unauthorized response
            connection.addRequestProperty("User-Agent",USER_AGENT);
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode < 200 || statusCode > 299) {
                log.error("Unreachable URL {}", url);
                throw new InvalidUrlException("Unreachable URL: " + url);
            }
        } catch (MalformedURLException e) {
            log.error("Malformed URL {} ", url, e);
            throw new InvalidUrlException("Malformed URL: " + url, e);
        } catch (IOException e) {
            log.error("Failed to open connection to URL {} ", url, e);
            throw new InvalidUrlException("Failed to open connection to URL: " + url, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Shortens the provided URL by generating a SHA-256 hash of it and then converting the hash to a base 62 string.
     *
     * @param originalUrl the URL to shorten
     * @return a shortened version of the URL
     * @throws RuntimeException if the SHA-256 hash algorithm is not found
     */
    public static String shortenUrl(String originalUrl){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            BigInteger hashInt = new BigInteger(1, hash);
            return toBase62(hashInt).substring(0, 6);

        } catch (NoSuchAlgorithmException e) {
            log.error("Hash not found {} ", HASH_ALGORITHM, e);
            throw new RuntimeException(HASH_ALGORITHM + " not found",e);
        }
    }

    /**
     * Converts a BigInteger to a base62 string.
     * <p>
     * The conversion is done by repeatedly dividing the number by 62 and
     * then mapping the remainder to a character in the base62 alphabet
     * (0-9, a-z, A-Z). The process continues until the BigInteger reaches zero.
     *
     * @param value the BigInteger to convert
     * @return the base62 string representation of the BigInteger
     */
    private static String toBase62(BigInteger value) {
        StringBuilder sb = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            int index = value.mod(BigInteger.valueOf(62)).intValue();
            sb.append(BASE62.charAt(index));
            value = value.divide(BigInteger.valueOf(62));
        }
        return sb.toString();
    }
}
