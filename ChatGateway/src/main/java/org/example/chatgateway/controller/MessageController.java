package org.example.chatgateway.controller;

import org.example.chatgateway.grpc.StorageGrpcClient;
import org.example.chatgateway.kafka.KafkaProducers;
import org.example.chatgateway.util.JsonUtil;
import org.example.messagestorage.grpc.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    private final StorageGrpcClient storageGrpcClient;
    private final KafkaProducers kafkaProducers;

    public MessageController(StorageGrpcClient storageGrpcClient,
                             KafkaProducers kafkaProducers) {
        this.storageGrpcClient = storageGrpcClient;
        this.kafkaProducers = kafkaProducers;
    }

    // Формат сообщения, которое приходит из WebSocket
    public record ClientMessage(String chatId, List<String> toUserIds, String type, String text) {}

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ClientMessage msg, SimpMessageHeaderAccessor headers) {

        String fromUserId = (String) headers.getSessionAttributes().get("userId");
        if (fromUserId == null) return;

        var stub = storageGrpcClient.getStub();

        SaveMessageRequest request = SaveMessageRequest.newBuilder()
                .setChatId(msg.chatId())
                .setFromUserId(fromUserId)
                .addAllToUserIds(msg.toUserIds())
                .setBody(
                        MessageBody.newBuilder()
                                .setType(msg.type() == null ? "text" : msg.type())
                                .setText(msg.text() == null ? "" : msg.text())
                                .build()
                )
                .build();

        SaveMessageResponse saved = stub.saveMessage(request);

        Map<String, Object> event = Map.of(
                "event", "chat.message.new",
                "messageId", saved.getMessageId(),
                "chatId", msg.chatId(),
                "fromUserId", fromUserId,
                "toUserIds", msg.toUserIds(),
                "body", Map.of("type", msg.type(), "text", msg.text()),
                "createdAt", saved.getCreatedAt()
        );

        kafkaProducers.publishMessageNew(msg.chatId(), JsonUtil.toJson(event));
    }
}
