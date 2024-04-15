package org.commonweb.security;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:8080") // 허용할 오리진
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
                .allowCredentials(true);
    }
}
