package org.laokouyun.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.grpc.client.GrpcClientFactory;

@Configuration
class GrpcConfig {

    @Bean
    GrpcClientFactory grpcClientFactory(GrpcChannelFactory grpcChannelFactory) {
        StaticApplicationContext context = new StaticApplicationContext();
        GrpcClientFactory grpcClientFactory = new GrpcClientFactory();
        context.registerBean(GrpcChannelFactory.class, () -> grpcChannelFactory);
        grpcClientFactory.setApplicationContext(context);
        return grpcClientFactory;
    }

}
