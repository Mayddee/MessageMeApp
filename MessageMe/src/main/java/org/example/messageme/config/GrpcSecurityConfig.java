package org.example.messageme.config;

import io.grpc.Metadata;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.example.messageme.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
public class GrpcSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public GrpcSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Bean
    public GrpcAuthenticationReader authenticationReader() {

        return (call, headers) -> {

            // читаем заголовок Authorization
            String authHeader = headers.get(AUTHORIZATION_HEADER);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }

            String jwt = authHeader.substring(7);

            if (!jwtTokenProvider.validateToken(jwt)) {
                return null;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            return authentication;
        };
    }
}
