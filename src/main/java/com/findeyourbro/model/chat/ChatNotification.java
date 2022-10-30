package com.findeyourbro.model.chat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ChatNotification {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;
    private String senderId;
    private String senderName;
    
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getSenderId() {return senderId;}
    public void setSenderId(String senderId) {this.senderId = senderId;}
    public String getSenderName() {return senderName;}
    public void setSenderName(String senderName) {this.senderName = senderName;}
    
}
