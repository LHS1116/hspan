package com.hspan.hspan;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hspan")
public class HsConfig {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }
}
