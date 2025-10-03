package com.exe.Huerta_directa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Asegurar que /login siempre use el controlador, no recursos estáticos
        // Esta configuración tiene menor prioridad que @GetMapping en controladores
        registry.addViewController("/login-page").setViewName("login/login");
    }
}