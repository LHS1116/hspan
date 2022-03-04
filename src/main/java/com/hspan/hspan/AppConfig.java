package com.hspan.hspan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.modelmapper.ModelMapper;

import com.hspan.hspan.data.repos.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.DEFAULT;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public CloseableHttpClient closeableHttpClient() {
        return new CloseableHttpClient() {
            @Override
            protected CloseableHttpResponse doExecute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public HttpParams getParams() {
                return null;
            }

            @Override
            public ClientConnectionManager getConnectionManager() {
                return null;
            }
        };
    }

}

