package com.findeyourbro.model.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.findeyourbro.model.contact.Contact;
import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.preference.Preference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class User implements Serializable, UserDetails{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    private String password;
    @NotNull
    private String gender;
    @NotNull
    private LocalDate birthDate;
    @JsonIgnore
    private String profileImageKey;
    @Transient
    private String profileImageBase64;
    private String profileImageName;
    private String photo;
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name="user_preferences", joinColumns=
    {@JoinColumn(name="user_id")}, inverseJoinColumns=
    {@JoinColumn(name="preference_id")})
    private List<Preference> sports_interests;
    @Transient
    @JsonIgnore
    private boolean accountNonExpired;
    @Transient
    @JsonIgnore
    private boolean accountNonLocked;
    @Transient
    @JsonIgnore
    private boolean credentialsNonExpired;
    @Transient
    @JsonIgnore
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Contact> contacts;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Notification> notifications;
    private String bio;
    @Transient
    private List<Double> lateLng;
    @JsonIgnore
    private Double late;
    @JsonIgnore
    private Double lng;
      
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public LocalDate getBirthDate() {return birthDate;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}
    public String getProfileImageBase64() {return profileImageBase64;}
    public void setProfileImageBase64(String profileImageBase64) {this.profileImageBase64 = profileImageBase64;}
    public String getProfileImageName() {return profileImageName;}
    public void setProfileImageName(String profileImageName) {this.profileImageName = profileImageName;}  
    public String getProfileImageKey() {return profileImageKey;}
    public void setProfileImageKey(String profileImageKey) {this.profileImageKey = profileImageKey;}    
    public List<Preference> getSports_interests() {
        if(this.sports_interests == null) {
            this.sports_interests = new ArrayList<>();
        }
        return sports_interests;
    }
    public void setSports_interests(List<Preference> sports_interests) {
        this.sports_interests = sports_interests;
    }
    @Override
    public boolean isAccountNonExpired() {return this.accountNonExpired;}
    public void setAccountNonExpired(boolean accountNonExpired) {this.accountNonExpired = accountNonExpired;}
    @Override
    public boolean isAccountNonLocked() {return this.accountNonLocked;}
    public void setAccountNonLocked(boolean accountNonLocked) {this.accountNonLocked = accountNonLocked;}
    @Override
    public boolean isCredentialsNonExpired() {return this.credentialsNonExpired;}
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {this.credentialsNonExpired = credentialsNonExpired;}
    @Override
    public boolean isEnabled() {return this.enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
    @Override
    public String getUsername() {return this.email;}     
    public List<Contact> getContacts() {
        if(contacts == null) {
            contacts = new ArrayList<>();
        }
        return contacts;
    }
    public void setContacts(List<Contact> contacts) {this.contacts = contacts;} 
    public void addContact(Contact contact) {
        getContacts().add(contact);
    }
    public String getBio() {return bio;}
    public void setBio(String bio) {this.bio = bio;}
    public List<Double> getLateLng() { return lateLng; }
    public void setLateLng(List<Double> lateLng) { this.lateLng = lateLng;} 
    public Double getLate() {return late;}
    public void setLate(Double late) {this.late = late;}
    public Double getLng() {return lng;}
    public void setLng(Double lng) {this.lng = lng;}   
    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", gender="
                + gender + ", birthDate=" + birthDate + "]";
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return null;}
    public List<Notification> getNotifications() {
        if(notifications == null) {
            notifications = new ArrayList<>();
        }
        return notifications;
    }
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
    
    public void addNotification(Notification notificaion) {
        getNotifications().add(notificaion);
    }   
       
}