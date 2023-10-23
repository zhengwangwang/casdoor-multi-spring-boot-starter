package org.casbin.casdoor.client;

import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.service.Service;

public interface Client {
    String getOrganizationName();

    <T extends Service> T getService(Class<T> clazz);

    Config getConfig();
}
