//package org.example.chatgateway.config;
//
//
//import org.example.chatgateway.security.TokenHandshakeInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    private final TokenHandshakeInterceptor tokenHandshakeInterceptor;
//
//    @Autowired
//    public WebsocketConfig(TokenHandshakeInterceptor tokenHandshakeInterceptor) {
//        this.tokenHandshakeInterceptor = tokenHandshakeInterceptor;
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .addInterceptors(tokenHandshakeInterceptor)
//                .setAllowedOriginPatterns("*");
//
//        registry.addEndpoint("/ws-sockjs")
//                .addInterceptors(tokenHandshakeInterceptor)
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/queue", "/topic");
//        registry.setUserDestinationPrefix("/user");
//    }
//}
package org.example.chatgateway.config;

import org.example.chatgateway.security.TokenHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenHandshakeInterceptor tokenHandshakeInterceptor;

    @Autowired
    public WebsocketConfig(TokenHandshakeInterceptor tokenHandshakeInterceptor) {
        this.tokenHandshakeInterceptor = tokenHandshakeInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Обычный WebSocket endpoint
        registry.addEndpoint("/ws")
                .addInterceptors(tokenHandshakeInterceptor)
                .setAllowedOriginPatterns("*");

        // SockJS fallback endpoint
        registry.addEndpoint("/ws-chat")
                .addInterceptors(tokenHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setUserDestinationPrefix("/user");
    }
}