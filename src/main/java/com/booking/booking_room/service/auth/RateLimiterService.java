package com.booking.booking_room.service.auth;

import com.booking.booking_room.config.limit.LoginRateLimitConfig;
import com.booking.booking_room.config.system.Common;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.time.Duration;

@Service
public class RateLimiterService {
    private final ProxyManager<String> apiProxyManager;
    private final ProxyManager<String> loginProxyManager;

    public RateLimiterService(javax.cache.CacheManager jCacheManager) {
        Cache<String, byte[]> apiCache =
                jCacheManager.getCache(Common.RATE_LIMIT_CACHE);
        this.apiProxyManager = new JCacheProxyManager<>(apiCache);

        Cache<String, byte[]> loginCache =
                jCacheManager.getCache(Common.LOGIN_RATE_LIMIT_CACHE);
        this.loginProxyManager = new JCacheProxyManager<>(loginCache);
    }

//    // Dùng cho API thông thường
//    public Bucket resolveBucket(String userId) {
//        UserTier tier = getUserTier(userId);
//        Bandwidth bw = tier == UserTier.PRO ? TIER_PRO : TIER_FREE;
//        return apiProxyManager.getProxy(userId, () -> bw);
//    }


    public Bucket resolveBucketForLoginByIp(String ip) {
        String key = "login:ip:" + ip;

        return loginProxyManager.builder().build(key,
                () -> BucketConfiguration.builder()
                        .addLimit(Bandwidth.classic(5,
                                Refill.greedy(5, Duration.ofMinutes(1))))
                        .build()
        );
    }

    public Bucket resolveBucketForLoginByIp1(String ip) {
        String key = "login:ip:" + ip;

        return loginProxyManager.builder().build(key,
                () -> BucketConfiguration.builder()
                        .addLimit(LoginRateLimitConfig.LOGIN_BY_IP)
                        .build()
        );
    }
}