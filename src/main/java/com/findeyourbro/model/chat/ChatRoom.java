package com.findeyourbro.model.chat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
    
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getChatId() {return chatId;}
    public void setChatId(String chatId) {this.chatId = chatId;}
    public String getSenderId() {return senderId;}
    public void setSenderId(String senderId) {this.senderId = senderId;}
    public String getRecipientId() {return recipientId;}
    public void setRecipientId(String recipientId) {this.recipientId = recipientId;}
       
}
