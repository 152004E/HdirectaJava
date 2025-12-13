package com.exe.Huerta_directa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HuertaDirectaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuertaDirectaApplication.class, args);
    }

}
