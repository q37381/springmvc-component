package com.c2501.component.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.c2501.component.interceptor.ControllerInterceptor;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ControllerInterceptor controllerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerInterceptor);
    }

}
