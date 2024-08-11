package com.thxpapa.juneberrydiary.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.front.url}")
    private String frontUrl;
    private final long MAX_AGE_SECS = 3600;
    private static final String[] RESOURCE_LOCATION = {
            "classpath:/static/"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(RESOURCE_LOCATION)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
