package org.casbin.casdoor.config;

import org.casbin.casdoor.client.ClientManager;
import org.casbin.casdoor.client.ConfigProvider;
import org.casbin.casdoor.client.impl.DefaultClientManager;
import org.casbin.casdoor.client.impl.DefaultConfigProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = CasdoorClientProperties.class)
public class CasdoorAutoConfigure {

    @Bean
    public ClientManager clientManager(ConfigProvider configProvider){
        return new DefaultClientManager(configProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigProvider configProvider(CasdoorClientProperties casdoorConfiguration){
        return new DefaultConfigProvider(casdoorConfiguration.getClient());
    }
}
