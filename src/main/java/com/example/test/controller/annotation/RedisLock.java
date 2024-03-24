package com.example.test.controller.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock {

    String value();

    long waitTime() default 30;

    long leaseTime() default 100;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
