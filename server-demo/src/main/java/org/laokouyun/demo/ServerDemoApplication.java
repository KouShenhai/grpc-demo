package org.laokouyun.demo;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication implements CommandLineRunner {

    private final SimpleGrpc.SimpleBlockingStub stub;

    public ServerDemoApplication(SimpleGrpc.SimpleBlockingStub stub) {
        this.stub = stub;
    }

    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(stub.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
    }

}
