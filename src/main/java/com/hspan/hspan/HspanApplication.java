package com.hspan.hspan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({HsConfig.class})
public class HspanApplication {

    public static void main(String[] args) {
        SpringApplication.run(HspanApplication.class, args);
    }

}
