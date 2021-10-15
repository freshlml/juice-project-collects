package com.project.normal.test.spring.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextTypeMatchBeanConfig {

    //@Lazy
    @Bean
    public ContextTypeMatchGenericBean<String> testContextGenericBean1() {
        return new ContextTypeMatchGenericBean<>();
    }

    @Bean
    public ContextTypeMatchGenericBean<Integer> testContextGenericBean2() {
        return new ContextTypeMatchGenericBean<>();
    }

}
