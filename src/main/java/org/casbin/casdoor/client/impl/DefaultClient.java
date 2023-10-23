package org.casbin.casdoor.client.impl;

import org.casbin.casdoor.client.Client;
import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.service.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DefaultClient implements Client {
    private static final String PACKAGE_SEARCH_PATH = "classpath*:org/casbin/casdoor/service/**/*.class";

    private static final  MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    private final String organizationName;

    private Config config;

    private Map<Class<? extends Service>, Service> serviceMap = new HashMap<>();

    public DefaultClient(Config config) {
        this.config = config;
        this.organizationName = config.organizationName;
        initService();
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public String getOrganizationName(){
        return this.organizationName;
    }
    private void initService(){
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources(PACKAGE_SEARCH_PATH);
            for (Resource resource : resources) {
                MetadataReader metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);
                if(isService(metadataReader)){
                    createServiceInstance(metadataReader);
                }
            }
        } catch (IOException e) {
            throw new org.casbin.casdoor.exception.Exception("I/O failure during classpath scanning", e);
        }
    }

    private boolean isService(MetadataReader metadataReader){
        String superClassName = metadataReader.getClassMetadata().getSuperClassName();
        return "org.casbin.casdoor.service.Service".equals(superClassName);
    }

    private void createServiceInstance(MetadataReader metadataReader){
        String className = metadataReader.getClassMetadata().getClassName();
        try {
            Class<Service> clazz = (Class<Service>) Class.forName(className);

            Constructor<Service> constructor = clazz.getConstructor(Config.class);
            Service service = constructor.newInstance(config);
            this.serviceMap.put(clazz, service);
        } catch (Exception e) {
            throw new org.casbin.casdoor.exception.Exception("cannot initialize service", e);
        }
    }
    @Override
    public <T extends Service> T getService(Class<T> clazz){
        Service service = this.serviceMap.get(clazz);
        if(service == null){
            throw new org.casbin.casdoor.exception.Exception("cannot find service in client: " + clazz.getName());
        }
        return (T)service;
    }
}
