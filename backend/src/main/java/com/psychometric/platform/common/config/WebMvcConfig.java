package com.psychometric.platform.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                List<MediaType> supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                MediaType utf8Json = new MediaType("application", "json", StandardCharsets.UTF_8);
                if (!supportedMediaTypes.contains(utf8Json)) {
                    supportedMediaTypes.add(utf8Json);
                }
                if (!supportedMediaTypes.contains(MediaType.APPLICATION_JSON)) {
                    supportedMediaTypes.add(MediaType.APPLICATION_JSON);
                }
                jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
                jacksonConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }
}
