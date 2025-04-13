package com.moviehouse.movieinfoservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

public abstract class ServiceDiscovery {
    @Autowired
    private DiscoveryClient discoveryClient;

    public String serviceUrl(String serviceName) {

        List<ServiceInstance> list = discoveryClient.getInstances(serviceName);
        if (list != null && list.size() > 0 ) {
            return list.get(0).getUri().toString();
        }
        return null;
    }
}
