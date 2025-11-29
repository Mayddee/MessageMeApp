package org.example.messagestorageservice.repository;

import org.example.messagestorageservice.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, String> {}

