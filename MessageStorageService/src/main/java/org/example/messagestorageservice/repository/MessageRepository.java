package org.example.messagestorageservice.repository;

import org.example.messagestorageservice.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
    List<MessageEntity> findByChatIdOrderByCreatedAtAsc(String chatId);
}
