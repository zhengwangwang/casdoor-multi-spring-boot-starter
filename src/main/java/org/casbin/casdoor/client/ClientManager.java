package org.casbin.casdoor.client;

import org.casbin.casdoor.client.impl.DefaultClient;
import org.casbin.casdoor.service.Service;

public interface ClientManager {

    <T extends Service> T getService(String organizationName, Class<T> tClass);

    Client getClient(String organizationName);

    void addClient(Client client);
}
