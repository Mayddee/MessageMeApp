package org.example.messagestorageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chat_participants")
@Data
public class ChatParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String chatId;

    private String userId;
}
