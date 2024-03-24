package com.example.test.controller.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.ast.Var;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author wangpeixian
 * @date 2024/01/07 11:16
 **/

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redisLock)")
    public Object aroundRedisLock(ProceedingJoinPoint point, RedisLock redisLock) throws Throwable{
        log.info("正在尝试请求获取锁");
        String value = redisLock.value();
        Assert.hasText(value, "@RedisLock key不能为空");
        String lockKey = value;
        log.info("解析后的lockKey为：{}", lockKey);
        long waitTime = redisLock.waitTime();
        long leaseTime = redisLock.leaseTime();
        TimeUnit timeUnit = redisLock.timeUnit();
        RLock lock = redissonClient.getLock(lockKey);
        // 尝试去获取锁
        boolean tryLock = lock.tryLock();
        if (!tryLock) {
            throw new Exception("锁被占用，请稍后提交！");
        }
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            log.error("方法执行失败：", throwable.getMessage());
        } finally {
            lock.unlock();
        }
        return point.proceed();
    }

}