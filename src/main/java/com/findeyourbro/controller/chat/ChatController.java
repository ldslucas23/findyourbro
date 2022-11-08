package com.findeyourbro.controller.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.findeyourbro.model.chat.ChatMessage;
import com.findeyourbro.model.chat.ChatNotification;
import com.findeyourbro.service.chat.ChatMessageService;
import com.findeyourbro.service.chat.ChatRoomService;

@Controller
@CrossOrigin("*")
public class ChatController {
   
    private SimpMessagingTemplate messagingTemplate;
    private ChatMessageService chatMessageService;
    private ChatRoomService chatRoomService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService,
            ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }
    
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);
        
        ChatNotification chatNotification = new ChatNotification();
        chatNotification.setId(saved.getId());
        chatNotification.setSenderId(saved.getSenderId());
        chatNotification.setSenderName(saved.getSenderName());
        
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()),"/queue/messages",
                chatNotification);
    }
}

