package com.dutaduta.sketchme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class SketchmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SketchmeApplication.class, args);
    }
}
