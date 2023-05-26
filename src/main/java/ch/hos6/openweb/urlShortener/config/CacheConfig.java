package ch.hos6.openweb.urlShortener.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ch.hos6.openweb.urlShortener.errorhandling.CustomCacheErrorHandler;

import java.time.Duration;

/**
 * Configuration class for Redis Cache.
 *
 * @author Toubia Oussama
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    /**
     * Time to live for cache entries.
     */
    @Value("${spring.cache.redis.time-to-live}")
    private int timeToLiveInHours = 1;

    public static final String URL_CACHE_NAME = "urls";

    /**
     * Creates the default cache configuration.
     *
     * @return the cache configuration
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(timeToLiveInHours))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * Creates the cache manager.
     *
     * @param redisConnectionFactory the Redis connection factory
     * @param cacheConfiguration     the cache configuration
     * @return the cache manager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration cacheConfiguration) {
        try {
            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(cacheConfiguration).build();
        } catch (Exception e) {
            log.error("Unable to build the cache: " + e.getMessage());
            //we can return some fallback cache, but not in this case to keep simple implementation
            //we just throw the exception

            throw e;
        }
    }

    /**
     * This method overrides the {@link org.springframework.cache.interceptor.CacheErrorHandler}
     * from the Spring CacheManager and provides a custom error handling strategy.
     *
     * @return CustomCacheErrorHandler, a custom error handling strategy for cache operations
     * (just logging to keep it simple).
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

}
