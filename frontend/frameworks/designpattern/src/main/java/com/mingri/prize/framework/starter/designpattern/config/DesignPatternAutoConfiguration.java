package com.mingri.prize.framework.starter.designpattern.config;

import com.mingri.prize.framework.starter.designpattern.chain.AbstractChainContext;
import com.mingri.prize.framework.starter.web.config.WebAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设计模式自动装配
 */
@Configuration
//@ImportAutoConfiguration(WebAutoConfiguration.class)
public class DesignPatternAutoConfiguration {

    /**
     * 责任链上下文
     */
    @Bean
    public AbstractChainContext abstractChainContext() {
        return new AbstractChainContext();
    }


}
