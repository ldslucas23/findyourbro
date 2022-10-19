package com.findeyourbro.model;

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
    private int contactId;
    @Transient
    private User contacts;
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public int getContactId() {return contactId;}
    public void setContactId(int contactId) {this.contactId = contactId;}
    
}
