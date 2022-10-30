package com.findeyourbro.model.contact;

import com.findeyourbro.model.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Contact {
    
    @Id
    @GeneratedValue
    private Long id;
    private Long contactId;
    @Transient
    private User user;
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Long getContactId() { return contactId;}
    public void setContactId(Long contactId) { this.contactId = contactId;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}  
}
