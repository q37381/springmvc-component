package com.c2501.component.configuration;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SpringConfiguration {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(SDF);
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
