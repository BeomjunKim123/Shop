package com.mysite.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 파일을 업로드시 서버의 경로와 물리적인 경로를 매핑 설정 


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")   //application.properties 파일의 변수를 로딩 
    String uploadPath;			//uploadPath = file:///C:/shop/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")		// 서버에서 처리되는 경로 
                .addResourceLocations(uploadPath);		//실제 이미지가 저장되는 물리적 경로
    }

    
    
    
}