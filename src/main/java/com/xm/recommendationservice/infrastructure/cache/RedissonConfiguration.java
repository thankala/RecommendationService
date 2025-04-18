package com.xm.recommendationservice.infrastructure.cache;

import java.time.Duration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import com.giffing.bucket4j.spring.boot.starter.config.cache.SyncCacheResolver;
// import com.giffing.bucket4j.spring.boot.starter.config.cache.jcache.JCacheCacheResolver;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.redisson.Bucket4jRedisson;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;

// import io.github.bucket4j.distributed.proxy.ProxyManager;
// import io.github.bucket4j.grid.jcache.JCacheProxyManager;

@Configuration
public class RedissonConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://%s:%s".formatted(redisHost, redisPort)); // or use env var
        return Redisson.create(config);
    }

    // @Bean
	// CommandAsyncExecutor getCommandAsyncExecutor() {
    //     var address = "redis://%s:%s".formatted(redisHost, redisPort);

    //     var config = new Config();
    //     config.useSingleServer()
    //             .setAddress(address)
    //             .setRetryAttempts(5);
	// 	return ((Redisson)Redisson.create(config)).getCommandExecutor();
    // }

    @Bean
    public RedissonBasedProxyManager<String> redissonBasedProxyManager(RedissonClient redissonClient) {
        CommandAsyncExecutor executor = ((Redisson) redissonClient).getCommandExecutor();

        return Bucket4jRedisson.casBasedBuilder(executor)
            .expirationAfterWrite(ExpirationAfterWriteStrategy
                .basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1)))
            .keyMapper(Mapper.STRING)
            .build();
    }

    // @Bean
    // public CacheManager cacheManager(RedissonClient redissonClient) {
    //     return new RedissonSpringCacheManager(redissonClient);
    // }
}

// @Configuration
// public class RedissonConfiguraiton {

//     @Value("${spring.redis.host}")
//     private String redisHost;
//     @Value("${spring.redis.port}")
//     private String redisPort;

//     @Bean
// 	CommandAsyncExecutor getCommandAsyncExecutor() {
//         var address = "redis://%s:%s".formatted(redisHost, redisPort);

//         var config = new Config();
//         config.useSingleServer()
//                 .setAddress(address)
//                 .setRetryAttempts(5);
// 		return ((Redisson)Redisson.create(config)).getCommandExecutor();
//     }

// }