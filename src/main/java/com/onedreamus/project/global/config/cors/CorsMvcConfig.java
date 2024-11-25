package com.onedreamus.project.global.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        //TDODO: 현재 주소는 임시 값 -> 추후 수정 필요
        corsRegistry.addMapping("/**")
            .allowedOrigins("http://localhost:3000");
    }
}
