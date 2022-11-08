package com.findeyourbro.service.chat;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.chat.ChatRoom;
import com.findeyourbro.repository.ChatRoomRepository;

@Service
public class ChatRoomService {

    private ChatRoomRepository chatRoomRepository;
    
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }
    
    public Optional<String> getChatId(
            Long senderId, Long recipientId, boolean createIfNotExist) {

         return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                 .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                     var chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = new ChatRoom();
                    senderRecipient.setId(UUID.randomUUID().toString());
                    senderRecipient.setSenderId(senderId);
                    senderRecipient.setChatId(chatId);
                    senderRecipient.setRecipientId(recipientId);

                    ChatRoom recipientSender = new ChatRoom();
                    recipientSender.setId(UUID.randomUUID().toString());
                    recipientSender.setChatId(chatId);
                    recipientSender.setSenderId(recipientId);
                    recipientSender.setRecipientId(senderId);

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
