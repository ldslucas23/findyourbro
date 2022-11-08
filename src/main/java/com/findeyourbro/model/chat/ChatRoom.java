package com.findeyourbro.model.chat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ChatRoom {
    
    @Id
    private String id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
    
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getChatId() {return chatId;}
    public void setChatId(String chatId) {this.chatId = chatId;}

    public Long getSenderId() {
        return senderId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public Long getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

       
}
