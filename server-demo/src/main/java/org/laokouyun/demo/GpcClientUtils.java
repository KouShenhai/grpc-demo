package org.laokouyun.demo;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractBlockingStub;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GpcClientUtils {

    private final Map<String, Object> stubMap = new ConcurrentHashMap<>();

    private final LoadBalancerClient loadBalancerClient;

    private final GrpcClientFactory grpcClientFactory;

    private int maxRetry = 3;
    private int waitMillis = 100;

    public <T extends AbstractBlockingStub<T>> Object invoke(String serviceId, Class<T> clazz, Method method, Object...args) throws Exception {
        for (int attempt  = 1; attempt  <= maxRetry; attempt++) {
            try {
                return ReflectionUtils.invokeMethod(method, getStub(serviceId, clazz), args);
            } catch (Exception ex) {
                if (!(ex instanceof StatusRuntimeException)) {
                    throw ex;
                } else {
                    Thread.sleep(Duration.ofMillis(waitMillis));
                }
            }
        }
        return ReflectionUtils.invokeMethod(method, FallbackFactory.getFallback(clazz), args);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractBlockingStub<T>> T getStub(String serviceId, Class<T> clazz) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        if (serviceInstance == null) {
            throw new IllegalStateException(serviceId + " is not available");
        }
        String target = String.format("%s:%s", serviceInstance.getHost(), serviceInstance.getMetadata().getOrDefault("grpc_port", "9090"));
        String key = String.format("%s:%s:%s", serviceId, clazz.getName(), target);
        return (T) stubMap.computeIfAbsent(key, _ -> grpcClientFactory.getClient(target, clazz, null));
    }


}
