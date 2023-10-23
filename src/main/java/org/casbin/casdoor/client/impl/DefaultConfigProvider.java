package org.casbin.casdoor.client.impl;

import org.casbin.casdoor.client.ConfigProvider;
import org.casbin.casdoor.config.CasdoorClientProperties;
import org.casbin.casdoor.config.Config;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultConfigProvider implements ConfigProvider {
    private Map<String, Config> config = new HashMap<>();


    public DefaultConfigProvider(Map<String, CasdoorClientProperties.ConfigWrapper> config) {
        config.forEach((k, v) ->{
            this.config.put(v.getOrganizationName(), new Config(
                    v.getEndpoint(),
                    v.getClientId(),
                    v.getClientSecret(),
                    v.getCertificate(),
                    v.getOrganizationName(),
                    v.getApplicationName()
            ));
        });
    }

    @Override
    public Config getConfig(String organizationName) {
        return config.get(organizationName);
    }

    @Override
    public List<Config> getConfigs() {
        Collection<Config> values = config.values();
        List<Config> configs = values.stream().collect(Collectors.toList());
        return configs;
    }
}
