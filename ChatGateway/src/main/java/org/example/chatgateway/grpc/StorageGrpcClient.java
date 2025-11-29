package org.example.chatgateway.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.messagestorage.grpc.MessageStorageServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageGrpcClient {

    private final MessageStorageServiceGrpc.MessageStorageServiceBlockingStub stub;

    public StorageGrpcClient(
            @Value("${message.storage.host}") String host,
            @Value("${message.storage.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = MessageStorageServiceGrpc.newBlockingStub(channel);
    }

    public MessageStorageServiceGrpc.MessageStorageServiceBlockingStub getStub() {
        return stub;
    }
}
