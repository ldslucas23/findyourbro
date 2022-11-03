package com.findeyourbro.model.notification;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private NotificationEnum type;
    private Date datetime;
    private Long owner;
    private Long recipient;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}
    public String getTitle() {return title; }
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public NotificationEnum getType() {return type;}
    public void setType(NotificationEnum type) {this.type = type;}
    public Date getDatetime() {return datetime;}
    public void setDatetime(Date datetime) {this.datetime = datetime;}
    public Long getOwner() {return owner;}
    public void setOwner(Long owner) {this.owner = owner;}
    public Long getRecipient() {return recipient;}
    public void setRecipient(Long recipient) {this.recipient = recipient;} 
    
}
