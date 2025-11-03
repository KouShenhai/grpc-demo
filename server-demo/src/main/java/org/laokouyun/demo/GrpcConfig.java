package org.laokouyun.demo;


import org.laokouyun.demo.proto.SimpleGrpc;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfig {


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    SimpleGrpc.SimpleBlockingV2Stub simpleBlockingV2Stub(GrpcChannelFactory channelFactory) {
        return SimpleGrpc.newBlockingV2Stub(channelFactory.createChannel("localhost:9097"));
    }

}
