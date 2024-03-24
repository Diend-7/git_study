package com.example.test.controller.annotation;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpeixian
 * @date 2024/03/17 10:37
 **/

@Slf4j
@Component
@AllArgsConstructor
@Aspect
@RequiredArgsConstructor
public class WebLogAspect {

    private String method;

    @Pointcut("@annotation(Weblog)")
    public void myPointCut() {
    }

    @Around(value = "myPointCut()")
    public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Weblog annotation = signature.getMethod().getAnnotation(Weblog.class);
        log.info("调用方法{}， 注解参数{}", methodName, annotation.methodName());

        Map<String, Object> requestParam = getRequestParam(joinPoint);
        log.info("请求参数为：{}",requestParam);

        Object proceed = joinPoint.proceed();
        log.info("调用方法{}， 出参为{}", methodName, proceed);
        return proceed;
    }

    public String getMethodName(JoinPoint joinPoint) {
        if (StringUtils.isEmpty(method)) {
            method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        }
        return method;
    }

    public Map<String, Object> getRequestParam(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("请求的url为：{}", request.getRequestURL().toString());
        log.info("Http method:{}", request.getMethod());
        log.info("IP:{}", request.getRemoteAddr());

        Enumeration<String> parameterNames = request.getParameterNames();
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();

        while(parameterNames.hasMoreElements()) {
            String s = parameterNames.nextElement();
            String parameter = request.getParameter(s);
            stringObjectHashMap.put(s, parameter);
        }
        return stringObjectHashMap;
    }
}