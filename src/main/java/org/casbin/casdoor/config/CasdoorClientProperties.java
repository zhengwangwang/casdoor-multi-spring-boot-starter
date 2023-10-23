package org.casbin.casdoor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑旺旺
 */

@ConfigurationProperties(prefix = "casdoor")
public class CasdoorClientProperties {
    private  Map<String, ConfigWrapper> client = new HashMap<>();

    public Map<String, ConfigWrapper> getClient() {
        return client;
    }

    @Data
    public static class ConfigWrapper{
        private String endpoint;
        private String clientId;
        private String clientSecret;
        private String certificate;
        private String organizationName;
        private String applicationName;
    }
}
