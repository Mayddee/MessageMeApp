package org.example.chatgateway.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatgateway.grpc.AuthGrpcClient;
import org.example.chatgateway.grpc.StorageGrpcClient;
import org.example.messagestorage.grpc.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.messageme.grpc.auth.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRestController {

    private final StorageGrpcClient storageGrpcClient;
    private final AuthGrpcClient authGrpcClient;

    private String getUserIdFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new RuntimeException("Missing Authorization header");

        String token = authHeader.substring(7);
        var stub = authGrpcClient.getStub();

        ValidateTokenResponse res = stub.validateToken(
                ValidateTokenRequest.newBuilder()
                        .setToken(token)
                        .build()
        );

        if (!res.getValid())
            throw new RuntimeException("Invalid token");

        return String.valueOf(res.getUserId());
    }

    @PostMapping("/start-direct")
    public ResponseEntity<?> startDirectChat(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String, String> body
    ) {
        String currentUserId = getUserIdFromHeader(header);
        String otherUserId = body.get("otherUserId");

        if (otherUserId == null || otherUserId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "otherUserId is required"));
        }

        var storage = storageGrpcClient.getStub();

        // 1. Получаем все чаты пользователя
        var chatsRes = storage.getChatsForUser(
                GetChatsRequest.newBuilder()
                        .setUserId(currentUserId)
                        .build()
        );

        var desiredSet = List.of(currentUserId, otherUserId).stream().sorted().toList();

        String foundChatId = null;

        for (var chat : chatsRes.getChatsList()) {
            if (!"private".equalsIgnoreCase(chat.getType())) continue;

            var participantsSorted = chat.getParticipantsList().stream().sorted().toList();
            if (participantsSorted.equals(desiredSet)) {
                foundChatId = chat.getChatId();
                break;
            }
        }

        // 2. Если чат уже есть – возвращаем его
        if (foundChatId != null) {
            return ResponseEntity.ok(Map.of(
                    "chatId", foundChatId,
                    "type", "private",
                    "userIds", desiredSet
            ));
        }

        // 3. Если нет – создаем новый
        var createReq = CreateChatRequest.newBuilder()
                .setTitle("Direct chat")
                .setType("private")
                .addUserIds(currentUserId)
                .addUserIds(otherUserId)
                .build();

        var createRes = storage.createChat(createReq);

        return ResponseEntity.ok(Map.of(
                "chatId", createRes.getChatId(),
                "type", "private",
                "userIds", desiredSet
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChat(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String, Object> body
    ) {
        String userId = getUserIdFromHeader(header);
        var storage = storageGrpcClient.getStub();

        @SuppressWarnings("unchecked")
        var userIds = (List<String>) body.get("userIds");
        if (!userIds.contains(userId)) userIds.add(userId);

        var req = CreateChatRequest.newBuilder()
                .addAllUserIds(userIds)
                .setTitle((String) body.get("title"))
                .setType((String) body.get("type"))
                .build();

        var res = storage.createChat(req);

        return ResponseEntity.ok(Map.of("chatId", res.getChatId()));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getChats(@RequestHeader("Authorization") String header) {
        String userId = getUserIdFromHeader(header);
        var storage = storageGrpcClient.getStub();

        var res = storage.getChatsForUser(
                GetChatsRequest.newBuilder()
                        .setUserId(userId)
                        .build()
        );

        List<Map<String, Object>> chats = res.getChatsList().stream()
                .map(chat -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("chatId", chat.getChatId());
                    map.put("title", chat.getTitle());
                    map.put("type", chat.getType());
                    map.put("userIds", chat.getParticipantsList());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getMessages(
            @RequestHeader("Authorization") String header,
            @PathVariable String chatId
    ) {
        String userId = getUserIdFromHeader(header);
        var storage = storageGrpcClient.getStub();

        var res = storage.getChatMessages(
                GetMessagesRequest.newBuilder()
                        .setChatId(chatId)
                        .build()
        );

        List<Map<String, Object>> messages = res.getMessagesList().stream()
                .map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", msg.getId());
                    map.put("fromUserId", msg.getFromUserId());
                    map.put("body", msg.getBody());
                    map.put("createdAt", msg.getCreatedAt());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }
}