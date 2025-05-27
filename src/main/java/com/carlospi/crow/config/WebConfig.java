package com.carlospi.crow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String avatarUploadPath = Paths.get("uploads/avatars").toAbsolutePath().toUri().toString();
        String crowGalleryPath = Paths.get("uploads/crow-gallery").toAbsolutePath().toUri().toString();
        String recompensasPath = Paths.get("uploads/recompensas").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations(avatarUploadPath);

        registry.addResourceHandler("/crow-gallery/**")
                .addResourceLocations(crowGalleryPath);

        registry.addResourceHandler("/recompensas/**")
                .addResourceLocations(recompensasPath);
    }
}