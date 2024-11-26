package com.onedreamus.project.global.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        //TDODO: 보안 문제 개선 -> 필요한 HTTP 메서드, 요청 헤더만 사용
        corsRegistry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",       // 개발 환경 도메인
                "https://thisismoney.site"            // 배포 환경 도메인
            )
            .allowedMethods("*")               // 모든 HTTP 메서드 허용
            .allowedHeaders("*")               // 모든 헤더 허용
            .allowCredentials(true);           // 쿠키와 인증 정보 허용 (필요한 경우 추가)
    }
}
