package com.findeyourbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.chat.ChatMessage;
import com.findeyourbro.model.chat.MessageStatus;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
    
    @Query("SELECT s FROM ChatMessage s WHERE s.senderId = ?1 and s.recipientId = ?2")
    List<ChatMessage> findBySenderIdAndrecipientId(Long senderId, Long recipientId);
        
}
