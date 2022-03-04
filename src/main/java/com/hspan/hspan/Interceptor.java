package com.hspan.hspan;

import com.hspan.hspan.services.LoginAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Interceptor implements WebMvcConfigurer {


    @Bean
    LoginAuthInterceptor loginAuthInterceptor() {
        return new LoginAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，排除登录url
        registry.addInterceptor(loginAuthInterceptor()).excludePathPatterns("/api/**");
//                .excludePathPatterns("/api/user/**").excludePathPatterns("/api/login/**").excludePathPatterns("/api/share/**");

    }
}
