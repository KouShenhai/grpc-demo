package org.laokouyun.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laokouyun.demo.proto.HelloWorldProto;
import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.stereotype.Service;

import io.grpc.stub.StreamObserver;

@Service
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

	private static final Log log = LogFactory.getLog(GrpcServerService.class);

	@Override
	public void sayHello(HelloWorldProto.HelloRequest req, StreamObserver<HelloWorldProto.HelloReply> responseObserver) {
		log.info("Hello " + req.getName());
		if (req.getName().startsWith("error")) {
			throw new IllegalArgumentException("Bad name: " + req.getName());
		}
		if (req.getName().startsWith("internal")) {
			throw new RuntimeException();
		}
		HelloWorldProto.HelloReply reply = HelloWorldProto.HelloReply.newBuilder().setMessage("Hello ==> " + req.getName()).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}