package org.casbin.casdoor.client;

import org.casbin.casdoor.config.Config;

import java.util.List;

public interface ConfigProvider {
    Config getConfig(String organizationName);

    List<Config> getConfigs();
}
