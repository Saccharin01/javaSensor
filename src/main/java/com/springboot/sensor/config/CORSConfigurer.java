//package com.springboot.sensor.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CORSConfigurer implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
//                .allowedOrigins("http://localhost:3000") // 프론트 도메인
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP 메서드
//                .allowedHeaders("*") // 헤더 제한 없음
//                .allowCredentials(true); // 쿠키 전송 허용 (프론트 fetch에 credentials: 'include' 필요)
//    }
//}