package com.exe.Huerta_directa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve la carpeta "uploads" desde la raíz del proyecto
        // Usamos "file:" para apuntar a ruta física
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
