package org.laokouyun.demo;

import io.grpc.stub.AbstractBlockingStub;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class GpcClientUtils {

    private final Map<String, Object> stubMap = new ConcurrentHashMap<>();

    private final LoadBalancerClient loadBalancerClient;

    private final GrpcClientFactory grpcClientFactory;

    @SuppressWarnings("unchecked")
    public <V> V invoke(Supplier<V> supplier, Object target, Method method, Object... args) {
        ReflectionUtils.makeAccessible(method);
        return (V) ReflectionUtils.invokeMethod(method, target, args);
    }

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
