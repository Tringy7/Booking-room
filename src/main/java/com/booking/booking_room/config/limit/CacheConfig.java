package com.booking.booking_room.config.limit;

import com.booking.booking_room.config.system.Common;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary; // Quan trọng

// Import CacheManager của JCache (JSR-107)
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    // --- CẤU HÌNH CHO BUCKET4J (JCACHE) ---
    @Bean
    public Config redissonConfig() {
        Config config = new Config();
        // Cấu hình kết nối Redis của bạn (Single, Cluster, Sentinel...)
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        return config;
    }

    /**
     * Bean này dành RIÊNG cho Bucket4j.
     * Bucket4j starter sẽ tự động tìm Bean có kiểu javax.cache.CacheManager.
     */
    @Bean
    @Primary
    public javax.cache.CacheManager jCacheManagerForBucket4j(Config redissonConfig) {
        CachingProvider provider = Caching.getCachingProvider();
        javax.cache.CacheManager cacheManager = provider.getCacheManager();

        // Config cho API — không TTL, userId tồn tại lâu dài
        javax.cache.configuration.Configuration<Object, Object> apiConfig =
                RedissonConfiguration.fromConfig(redissonConfig);

        // Config cho login — TTL 10 phút, tự dọn key cũ trên Redis
        javax.cache.configuration.MutableConfiguration<Object, Object> loginMutable =
                new javax.cache.configuration.MutableConfiguration<>()
                        .setExpiryPolicyFactory(
                                javax.cache.expiry.CreatedExpiryPolicy.factoryOf(
                                        new javax.cache.expiry.Duration(TimeUnit.MINUTES, 10)
                                )
                        );
        javax.cache.configuration.Configuration<Object, Object> loginConfig =
                RedissonConfiguration.fromConfig(redissonConfig, loginMutable);

        if (cacheManager.getCache(Common.RATE_LIMIT_CACHE) == null) {
            cacheManager.createCache(Common.RATE_LIMIT_CACHE, apiConfig);
            log.info("Cache '{}' initialized.", Common.RATE_LIMIT_CACHE);
        }
        if (cacheManager.getCache(Common.LOGIN_RATE_LIMIT_CACHE) == null) {
            cacheManager.createCache(Common.LOGIN_RATE_LIMIT_CACHE, loginConfig);
            log.info("Cache '{}' initialized.", Common.LOGIN_RATE_LIMIT_CACHE);
        }
        return cacheManager;
    }
}

