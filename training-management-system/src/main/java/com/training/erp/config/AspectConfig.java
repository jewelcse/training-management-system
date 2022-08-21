package com.training.erp.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
@Aspect
@Configuration
public class AspectConfig {
    Logger logger = LoggerFactory.getLogger(AspectConfig.class);


    // called every time before a controller method executed
    @Before("execution(* com.training.erp.controller.*.*(..))")
    public void beforeExecution(JoinPoint joinPoint) {
        logger.info("Executing {}",joinPoint);
    }
    // called every time after a controller method executed
    @After("execution(* com.training.erp.controller.*.*(..))")
    public void afterExecution(JoinPoint joinPoint) {
        logger.info("Completed {}",joinPoint);
    }

    // called every time if any exceptions occurred in the service
    @Around("execution(* com.training.erp.service.*.*(..))")
    public Object aroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            Object object = joinPoint.proceed();
            return object;
        }catch (Exception e) {
            logger.info("Handling exception while executing{}");
        }

        return null;
    }


}
