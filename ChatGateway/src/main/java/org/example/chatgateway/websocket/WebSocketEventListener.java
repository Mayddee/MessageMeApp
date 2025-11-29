package org.example.chatgateway.websocket;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @EventListener
    public void onConnect(SessionConnectEvent e) {
        var acc = StompHeaderAccessor.wrap(e.getMessage());
        System.out.println("User connected: " + acc.getSessionAttributes());
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent e) {
        System.out.println("Session disconnected: " + e.getSessionId());
    }
}

