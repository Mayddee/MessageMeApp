package org.example.chatgateway.config;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.messageme.grpc.auth.AuthServiceGrpc;
import org.example.messagestorage.grpc.MessageStorageServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PreDestroy;
@Configuration
public class GrpcClientsConfig {

    private ManagedChannel authChannel;
    private ManagedChannel storageChannel;

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceStub(
            @Value("${messageme.auth.host}") String host,
            @Value("${messageme.auth.port}") int port) {

        this.authChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return AuthServiceGrpc.newBlockingStub(authChannel);
    }

    @Bean
    public MessageStorageServiceGrpc.MessageStorageServiceBlockingStub messageStorageStub(
            @Value("${message.storage.host}") String host,
            @Value("${message.storage.port}") int port) {

        this.storageChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return MessageStorageServiceGrpc.newBlockingStub(storageChannel);
    }

    @PreDestroy
    public void shutdown() {
        if (authChannel != null) authChannel.shutdownNow();
        if (storageChannel != null) storageChannel.shutdownNow();
    }
}


