package org.laokouyun.demo;
import io.grpc.StatusException;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.server.service.GrpcServiceDiscoverer;

@SpringBootApplication(scanBasePackages = "org.laokouyun.**")
public class ServerDemoApplication implements CommandLineRunner {

    private final SimpleGrpc.SimpleBlockingV2Stub stub2;
    private final GrpcServiceDiscoverer grpcServiceDiscoverer;

    public ServerDemoApplication(SimpleGrpc.SimpleBlockingV2Stub stub2, GrpcServiceDiscoverer grpcServiceDiscoverer) {
        this.stub2 = stub2;
        this.grpcServiceDiscoverer = grpcServiceDiscoverer;
    }

    static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws StatusException {
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(stub2.sayHello(HelloWorldProto.HelloRequest.newBuilder().setName("test").build()));
        System.out.println(grpcServiceDiscoverer.findServices());
    }

}
