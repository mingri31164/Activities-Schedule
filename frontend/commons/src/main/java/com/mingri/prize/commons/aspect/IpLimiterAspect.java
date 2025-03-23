package com.mingri.prize.commons.aspect;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 基于ip + 请求方法 的限流切面
 */
//@Aspect
@Slf4j
//@RestController
public class IpLimiterAspect {

    private volatile double DEFAULT_LIMITER_COUNT_PER_SECOND = 2;

    Cache<String, RateLimiter> limiterCache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();

    @Autowired
    private HttpServletRequest request;

//    @Around("execution(* com.mingri.prize.api.action.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String ip = request.getHeader("X-Real-IP");

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String methodName = joinPoint.getTarget().getClass().getName() + "." + methodSignature.getClass();
        String recordKey = ip + "->" + methodName;
        RateLimiter rateLimiter = limiterCache.get(recordKey, () -> RateLimiter.create(DEFAULT_LIMITER_COUNT_PER_SECOND));
        if(! rateLimiter.tryAcquire()){
            log.error("ip -> method {} 单位请求次数过多", recordKey);
            throw new RuntimeException("操作过快");
        }
        return joinPoint.proceed();
    }


}
