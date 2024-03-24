package com.example.test.controller.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangpeixian
 * @date 2024/01/07 11:11
 **/

@Configuration
public class MyRedisConfig {


    private String address="redis://127.0.0.1:6379";

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissionClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(address);

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}