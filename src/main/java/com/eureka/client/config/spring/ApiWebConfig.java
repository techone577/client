package com.eureka.client.config.spring;

import com.eureka.client.config.surpport.Base64DecodingFilter;
import com.eureka.client.config.surpport.FrameWorkFilter;
import com.eureka.client.config.surpport.JsonMapperArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author techoneduan
 * @date 2018/12/28
 */
@Configuration
@ComponentScan(basePackages = "com.eureka.client.web.api")
@EnableWebMvc
public class ApiWebConfig implements WebMvcConfigurer {

    @Bean
    public JsonMapperArgumentResolver jsonMapperArgumentResolver () {
        JsonMapperArgumentResolver jsonMapperArgumentResolver = new JsonMapperArgumentResolver();
        return jsonMapperArgumentResolver;
    }

    @Override
    public void addArgumentResolvers (List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jsonMapperArgumentResolver());

    }

}
