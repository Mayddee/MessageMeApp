package org.example.messagestorageservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatMessageIncoming {
    private String messageId;
    private String chatId;
    private ChatUser sender;
    private List<String> recipientIds;
    private String content;
    private LocalDateTime timestamp;
    private String type;

    @Data
    public static class ChatUser {
        private String userId;
        private String username;
        private String displayName;
    }
}
