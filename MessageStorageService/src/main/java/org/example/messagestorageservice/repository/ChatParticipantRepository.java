package org.example.messagestorageservice.repository;

import org.example.messagestorageservice.entity.ChatParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipantEntity, String> {
    List<ChatParticipantEntity> findByUserId(String userId);
    List<ChatParticipantEntity> findByChatId(String chatId);
}
