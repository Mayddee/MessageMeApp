package org.example.messagestorageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chats")
@Data
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String type; // private / group
}

