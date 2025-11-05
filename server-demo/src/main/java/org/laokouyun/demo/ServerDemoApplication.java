package org.laokouyun.demo;
import lombok.RequiredArgsConstructor;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;

@EnableDiscoveryClient
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication implements CommandLineRunner {

    private final GpcClientUtils gpcClientUtils;

    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(Duration.ofSeconds(10));
        Method method = ReflectionUtils.findMethod(SimpleGrpc.SimpleBlockingV2Stub.class, "sayHello", HelloWorldProto.HelloRequest.class);
        assert method != null;
        ReflectionUtils.makeAccessible(method);
        for (int i = 0; i < 10; i++) {
            HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName("test").build();
            System.out.println(gpcClientUtils.invoke("server-demo", SimpleGrpc.SimpleBlockingV2Stub.class, method, request));
        }
    }

}
