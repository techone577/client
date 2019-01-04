package com.eureka.client.config.spring;

import com.eureka.client.config.surpport.Base64DecodingFilter;
import com.eureka.client.config.surpport.FrameWorkFilter;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * @author techoneduan
 * @date 2018/12/6
 */
@Configuration
public class ApiWebInitiallizer implements ServletContextInitializer {
    @Override
    public void onStartup (ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext apiDispatcherCtx = new AnnotationConfigWebApplicationContext();
        apiDispatcherCtx.register(ApiWebConfig.class);
        ServletRegistration.Dynamic apiDispatcher = servletContext.addServlet("web-api-dispatcher", new DispatcherServlet(apiDispatcherCtx));
        apiDispatcher.addMapping("/api/*");
        apiDispatcher.setLoadOnStartup(1);

        FilterRegistration.Dynamic base64DecodingFilter = servletContext.addFilter("base64DecodingFilter",Base64DecodingFilter.class);
        base64DecodingFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),false,"/api/*");

        FilterRegistration.Dynamic framefilter = servletContext.addFilter("frameworkFilter",FrameWorkFilter.class);
        framefilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),false,"/api/*");
    }
}
