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
    private Long id;
    private Long senderId;
    private String senderName;
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Long getSenderId() {return senderId;}
    public void setSenderId(Long senderId) {this.senderId = senderId;}
    public String getSenderName() {return senderName;}
    public void setSenderName(String senderName) {this.senderName = senderName;}
    
}
