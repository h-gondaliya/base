package com.netcarat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // Handle Swagger UI resources
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .resourceChain(false);
        
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .resourceChain(false);

        // Handle other static resources and Angular app
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        // Skip Swagger UI paths
                        if (resourcePath.startsWith("swagger-ui/") || 
                            resourcePath.startsWith("v3/api-docs/") ||
                            resourcePath.equals("swagger-ui.html")) {
                            return null;
                        }
                        
                        Resource requestedResource = location.createRelative(resourcePath);
                        if(requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}


