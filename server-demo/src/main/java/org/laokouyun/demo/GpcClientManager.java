package org.laokouyun.demo;

import io.grpc.stub.AbstractBlockingStub;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GpcClientManager {

    private final Map<String, Object> stubMap = new ConcurrentHashMap<>();

    private final LoadBalancerClient loadBalancerClient;

    private final GrpcClientFactory grpcClientFactory;

    @SuppressWarnings("unchecked")
    public <T extends AbstractBlockingStub<T>> T getStub(String serviceId, Class<T> clazz) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        if (serviceInstance == null) {
            throw new IllegalStateException(serviceId + " is not available");
        }
        String target = String.format("%s:%s", serviceInstance.getHost(), serviceInstance.getMetadata().getOrDefault("grpc_port", "9090"));
        String key = String.format("%s:%s:%s", serviceId, clazz.getName(), target);
        return (T) stubMap.computeIfAbsent(key, _ -> grpcClientFactory.getClient(target, clazz, null));
    }

}
