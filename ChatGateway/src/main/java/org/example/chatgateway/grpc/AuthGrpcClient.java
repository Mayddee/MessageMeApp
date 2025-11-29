
package org.example.chatgateway.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.messageme.grpc.auth.AuthServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthGrpcClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub stub;

    public AuthGrpcClient(
            @Value("${messageme.auth.host}") String host,
            @Value("${messageme.auth.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AuthServiceGrpc.AuthServiceBlockingStub getStub() {
        return stub;
    }
}


