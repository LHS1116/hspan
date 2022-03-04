package com.hspan.hspan;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hspan")
public class HsConfig {
    private String filePath;
    private String casServer;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCasServer() {
        return casServer;
    }

    public void setCasServer(String casServer) {
        this.casServer = casServer;
    }
}
