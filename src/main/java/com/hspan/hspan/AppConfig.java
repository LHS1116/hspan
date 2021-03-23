package com.hspan.hspan;

import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.modelmapper.ModelMapper;

import com.hspan.hspan.data.repos.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AppConfig {

    @Bean
    @RequestScope
    public Auth Auth(HttpServletRequest request,  UserRepository userRepository) {
        return new Auth(request, userRepository);
    }

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

}

