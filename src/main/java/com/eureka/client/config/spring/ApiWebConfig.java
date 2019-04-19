package com.eureka.client.config.spring;

import com.eureka.client.config.surpport.JsonMapperArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author techoneduan
 * @date 2018/12/28
 */
@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@ComponentScan(basePackages = "com.eureka.client.web.api")
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

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter(){
            @Override
            protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                if(object instanceof String){
                    Charset charset = this.getDefaultCharset();
                    StreamUtils.copy((String)object, charset, outputMessage.getBody());
                }else{
                    super.writeInternal(object, type, outputMessage);
                }
            }
        };
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
    }

}
