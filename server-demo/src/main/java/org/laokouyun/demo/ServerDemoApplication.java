package org.laokouyun.demo;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.server.service.GrpcServiceDiscoverer;

@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication implements CommandLineRunner {

    private final SimpleGrpc.SimpleBlockingStub stub;
    private final GrpcServiceDiscoverer grpcServiceDiscoverer;

    public ServerDemoApplication(SimpleGrpc.SimpleBlockingStub stub, GrpcServiceDiscoverer grpcServiceDiscoverer) {
        this.stub = stub;
        this.grpcServiceDiscoverer = grpcServiceDiscoverer;
    }

    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(stub.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(grpcServiceDiscoverer.findServices());
    }

}
