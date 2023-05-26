package ch.hos6.openweb.urlShortener.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;


/**
 * This class implements the {@link org.springframework.cache.interceptor.CacheErrorHandler} interface
 * and provides custom error handling for all cache operations (get, put, evict, clear).
 *
 * @author Toubia Oussama
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {
    /**
     * Handle errors during cache get operation.
     *
     * @param exception the exception that occurred.
     * @param cache the cache where the exception occurred.
     * @param key the cache key involved in the exception.
     */
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error with redis get operation cache {} and key {} . Ex = {}",cache.getName(),key.toString(),exception.getMessage());
    }

    /**
     * Handle errors during cache put operation.
     *
     * @param exception the exception that occurred.
     * @param cache the cache where the exception occurred.
     * @param key the cache key involved in the exception.
     * @param value the value intended for caching.
     */
    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("Error with redis put operation cache {} and key={}, value={}  . Ex = {}",cache.getName(),key.toString(),key.toString(),exception.getMessage());
    }

    /**
     * Handle errors during cache evict operation.
     *
     * @param exception the exception that occurred.
     * @param cache the cache where the exception occurred.
     * @param key the cache key involved in the exception.
     */
    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("Error with redis evict operation cache {} and key {} . Ex = {}",cache.getName(),key.toString(),exception.getMessage());

    }

    /**
     * Handle errors during cache clear operation.
     *
     * @param exception the exception that occurred.
     * @param cache the cache where the exception occurred.
     */
    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Error with redis clear operation cache {}. Ex = {}",cache.getName(),exception.getMessage());

    }
}
