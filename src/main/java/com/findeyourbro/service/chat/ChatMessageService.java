package com.findeyourbro.service.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.findeyourbro.model.chat.ChatMessage;
import com.findeyourbro.model.chat.MessageStatus;
import com.findeyourbro.repository.ChatMessageRepository;

@Service
public class ChatMessageService {
     private ChatMessageRepository repository;
     private ChatRoomService chatRoomService;
     
     public ChatMessageService(ChatMessageRepository repository, ChatRoomService chatRoomService) {
         this.repository = repository;
         this.chatRoomService = chatRoomService;
     }
    
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                }).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada: " +id, new Throwable()));

    }

    public void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
        
        ChatMessage sender = repository.findById(senderId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada", new Throwable()));
        sender.setStatus(status);
        
        ChatMessage recipient = repository.findById(recipientId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada", new Throwable()));
        sender.setStatus(status);
        
        repository.save(sender);
        repository.save(recipient);
    }
}
