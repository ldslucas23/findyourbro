package com.findeyourbro.model.group;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.findeyourbro.model.user.User;

@Entity
@Table
public class Event implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    @Transient
    private User owner;
    @JsonIgnore
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name="user_events", joinColumns=
    {@JoinColumn(name="event_id")}, inverseJoinColumns=
    {@JoinColumn(name="user_id")})
    private List<User> participants;
    @Transient
    private List<Double> lateLng;
    @JsonIgnore
    private Double late;
    @JsonIgnore
    private Double lng;
    @JsonIgnore
    private String profileImageKey;
    @Transient
    private String profileImageBase64;
    private String profileImageName;
    private String photo;
    private LocalDate date;
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Long getOwnerId() {return ownerId;}
    public void setOwnerId(Long ownerId) {this.ownerId = ownerId;}
    public List<User> getParticipants() {
        if(this.participants == null) {
            this.participants = new ArrayList<>();
        }
        return participants;
    }
    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
    public void addParticipant(User user) {
        getParticipants().add(user);
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public List<Double> getLateLng() {
        return lateLng;
    }
    public void setLateLng(List<Double> lateLng) {
        this.lateLng = lateLng;
    }
    public Double getLate() {
        return late;
    }
    public void setLate(Double late) {
        this.late = late;
    }
    public Double getLng() {
        return lng;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }
    public String getProfileImageKey() {
        return profileImageKey;
    }
    public void setProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }
    public String getProfileImageBase64() {
        return profileImageBase64;
    }
    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }
    public String getProfileImageName() {
        return profileImageName;
    }
    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }    
       
}
