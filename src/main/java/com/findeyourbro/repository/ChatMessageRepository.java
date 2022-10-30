package com.findeyourbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.chat.ChatMessage;
import com.findeyourbro.model.chat.MessageStatus;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}
