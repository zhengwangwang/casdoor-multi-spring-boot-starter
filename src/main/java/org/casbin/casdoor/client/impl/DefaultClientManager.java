package org.casbin.casdoor.client.impl;

import org.casbin.casdoor.client.Client;
import org.casbin.casdoor.client.ClientManager;
import org.casbin.casdoor.client.ConfigProvider;
import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.service.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultClientManager implements ClientManager {

    private ConfigProvider configProvider;

    private final Map<String, Client> clientMap = new HashMap();

    public DefaultClientManager(ConfigProvider configProvider) {
        Objects.requireNonNull(configProvider, "configProvider cannot be null !");
        this.configProvider = configProvider;
        initialize();
    }

    @Override
    public <T extends Service> T getService(String organizationName, Class<T> tClass){
        return getClient(organizationName).getService(tClass);
    }

    @Override
    public Client getClient(String organizationName){
        Client casdoorClient = clientMap.get(organizationName);
        if(casdoorClient != null){
            return casdoorClient;
        }
        Config config = configProvider.getConfig(organizationName);
        if(config == null){
            throw new org.casbin.casdoor.exception.Exception("cannot get client config from configProvider, organizationName: " + organizationName);
        }
        Client instance = new DefaultClient(config);
        clientMap.put(organizationName, instance);
        return instance;
    }

    @Override
    public void addClient(Client client){
        clientMap.put(client.getOrganizationName(), client);
    }

    private void initialize(){
        List<Config> configs = configProvider.getConfigs();
        configs.forEach(config -> {
            Client instance = new DefaultClient(config);
            clientMap.put(config.organizationName, instance);
        });
    }

}
