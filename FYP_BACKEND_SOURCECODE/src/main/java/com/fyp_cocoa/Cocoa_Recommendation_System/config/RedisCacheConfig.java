package com.fyp_cocoa.Cocoa_Recommendation_System.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig { //cache purpose

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        final RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10));
        return (builder) -> builder
                .withCacheConfiguration("flavorCache", redisCacheConfig)
                .withCacheConfiguration("productCache", redisCacheConfig)
                .withCacheConfiguration("companyCache", redisCacheConfig)
                .withCacheConfiguration("ingredientCache", redisCacheConfig);

    }
}
