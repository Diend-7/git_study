package com.example.test.controller;


import com.example.test.controller.annotation.RedisLock;
import com.example.test.controller.annotation.Weblog;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author wangpeixian
 * @date 2024/01/07 11:00
 **/

@RestController
public class RedisController {

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    @RedisLock("hello")
    @Weblog(methodName = "test")
    public String test() throws InterruptedException {
//        Thread.sleep(1000);
        return getName("wpx");
    }

    @Cacheable("wpx")
    public String getName(String name) {
        System.out.println(name);
        return name;
    }



}