package org.example.messagestorageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String chatId;
    private String fromUserId;

    @Column(columnDefinition = "text")
    private String toUserIds;

    @Column(columnDefinition = "text")
    private String bodyJson;

    private Instant createdAt;
}
