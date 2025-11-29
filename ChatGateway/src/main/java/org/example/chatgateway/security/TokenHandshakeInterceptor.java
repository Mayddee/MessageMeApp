package org.example.chatgateway.security;

import jakarta.servlet.http.HttpServletRequest;
import org.example.messageme.grpc.auth.AuthServiceGrpc;
import org.example.messageme.grpc.auth.ValidateTokenRequest;
import org.example.messageme.grpc.auth.ValidateTokenResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class TokenHandshakeInterceptor implements HandshakeInterceptor {

    private final AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public TokenHandshakeInterceptor(AuthServiceGrpc.AuthServiceBlockingStub authStub) {
        this.authStub = authStub;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (!(request instanceof ServletServerHttpRequest sreq)) {
            return false;
        }

        HttpServletRequest r = sreq.getServletRequest();

        String token = null;
        String authHeader = r.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token == null) {
            token = r.getParameter("token");
        }
        if (token == null) {
            return false;
        }

        ValidateTokenResponse res = authStub.validateToken(
                ValidateTokenRequest.newBuilder()
                        .setToken(token)
                        .build()
        );

        if (!res.getValid()) {
            return false;
        }

        attributes.put("userId", String.valueOf(res.getUserId()));
        attributes.put("username", res.getUsername());

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res,
                               WebSocketHandler wsHandler, Exception e) {
        // no-op
    }
}
