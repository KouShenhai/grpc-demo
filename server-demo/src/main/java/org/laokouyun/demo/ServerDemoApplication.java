package org.laokouyun.demo;
import io.grpc.StatusException;
import lombok.RequiredArgsConstructor;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import java.time.Duration;

@EnableDiscoveryClient
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication implements CommandLineRunner {

    private final GpcClientManager gpcClientManager;

    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws StatusException, InterruptedException {
        Thread.sleep(Duration.ofSeconds(10));
        for (int i = 0; i < 10; i++) {
            SimpleGrpc.SimpleBlockingV2Stub stub2 = gpcClientManager.getStub("server-demo", SimpleGrpc.SimpleBlockingV2Stub.class);
            HelloWorldProto.HelloRequest test = HelloWorldProto.HelloRequest.newBuilder().setName("test").build();
            System.out.println(stub2.sayHello(test));
        }
    }

}
