package com.findeyourbro.service.chat;

import java.util.Optional;

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
                    senderRecipient.setSenderId(String.valueOf(senderId));
                    senderRecipient.setChatId(chatId);
                    senderRecipient.setRecipientId(String.valueOf(recipientId));

                    ChatRoom recipientSender = new ChatRoom();
                    recipientSender.setChatId(chatId);
                    recipientSender.setSenderId(String.valueOf(recipientId));
                    recipientSender.setRecipientId(String.valueOf(senderId));

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
