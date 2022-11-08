package com.findeyourbro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.chat.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
   
    //@Query("SELECT s FROM chat_room chat_room WHERE s.recipient_id = ?2 AND s.sender_id = ?1")
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
