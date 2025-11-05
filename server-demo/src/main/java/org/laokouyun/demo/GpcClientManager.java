package org.laokouyun.demo;

import io.grpc.StatusException;
import io.grpc.stub.AbstractBlockingStub;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GpcClientManager {

    private final Map<String, Object> stubMap = new ConcurrentHashMap<>();

    private final LoadBalancerClient loadBalancerClient;

    private final GrpcClientFactory grpcClientFactory;

    public <V> V invoke(GrpcCallback<V> grpcCallback, Object target, Method method, Object...args) {
        Exception exception = new RuntimeException();
        V callback = null;
        for (int i = 1; i <= 3; i++) {
            if (!Objects.isNull(exception)) {
                try {
                    callback = grpcCallback.get();
                    exception = null;
                } catch (Exception ex) {
                    exception = ex;
                }
            }
        }
        if (exception instanceof StatusException) {
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, target, args);
        }
        return callback;
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
