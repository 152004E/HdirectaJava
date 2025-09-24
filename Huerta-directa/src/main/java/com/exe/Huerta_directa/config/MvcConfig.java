package com.exe.Huerta_directa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Aquí traes la propiedad que definiste en application.properties
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Se asigna la URL pública /images/productos/** para que apunte a la carpeta
        // local
        registry.addResourceHandler("/images/productos/**")
                .addResourceLocations("file:///" + uploadPath + "/productos/");
    }

}
