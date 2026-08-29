package com.moviehouse.userservice.client;

import com.moviehouse.userservice.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

public abstract class ServiceDiscovery {

    @Autowired
    private DiscoveryClient discoveryClient;

    public String serviceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new ServiceUnavailableException(serviceName + " is not registered with service discovery");
        }
        return instances.get(0).getUri().toString();
    }
}
