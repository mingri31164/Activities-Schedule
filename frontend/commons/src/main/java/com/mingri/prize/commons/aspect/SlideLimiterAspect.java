package com.mingri.prize.commons.aspect;

import com.mingri.prize.commons.annotition.SlideLimiter;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 滑动窗口限流切面实现
 */
@Aspect
@Slf4j
@Component
public class SlideLimiterAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private DefaultRedisScript<Long> limitScript;

    @PostConstruct
    public void init() throws IOException {
        limitScript = new DefaultRedisScript<>();
        limitScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("lua/slide_limiter.lua")));
        limitScript.setResultType(Long.class);
    }

    @Around("@annotation(slideLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, SlideLimiter slideLimiter) throws Throwable {
        // 构建限流唯一标识键
        String key = buildLimitKey(joinPoint, slideLimiter);
        Metrics.counter("limiter.requests", "key", key).increment();

        try {
            List<String> args = Arrays.asList(
                    key,
                    String.valueOf(slideLimiter.window()),
                    String.valueOf(slideLimiter.limit())
            );

            // 执行脚本（注意KEYS数组为空）
            Long result = redisTemplate.execute(
                    limitScript,
                    Collections.emptyList(),
                    args.toArray()
            );

            // 结果逻辑反转：返回1=允许，0=拒绝
            if (result != null && result == 0) {
                Metrics.counter("limiter.blocked").increment();
                throw new RuntimeException("请求频率超出限制！[窗口：" +
                        slideLimiter.window() + "秒, 限制：" +
                        slideLimiter.limit() + "次]");
            }

            Metrics.counter("limiter.passed").increment();
            return joinPoint.proceed();
        } catch (Exception e) {
            // 异常处理细化
            if (e instanceof RedisSystemException) {
                log.error("限流器Redis异常 | key={}", key, e);
                Metrics.counter("limiter.errors").increment();
                // 降级策略：根据业务需求选择放行或阻断
                return joinPoint.proceed();
            }
            throw e;
        }
    }



    private String buildLimitKey(ProceedingJoinPoint joinPoint, SlideLimiter slideLimiter) {
        // 解析SpEL表达式
        if (StringUtils.isNotEmpty(slideLimiter.dimension())) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            EvaluationContext context = new StandardEvaluationContext();

            // 设置参数变量
            Object[] args = joinPoint.getArgs();
            String[] paramNames = signature.getParameterNames();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            // 解析表达式
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(slideLimiter.dimension());
            Object value = exp.getValue(context);
            return slideLimiter.key() + value;
        }
        return slideLimiter.key();
    }


}
