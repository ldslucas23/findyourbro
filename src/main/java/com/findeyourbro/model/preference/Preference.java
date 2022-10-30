package com.findeyourbro.model.preference;

import com.findeyourbro.model.user.User;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Preference implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name="user_preferences", joinColumns=
    {@JoinColumn(name="preference_id")}, inverseJoinColumns=
      {@JoinColumn(name="user_id")})
    private List<User> users;
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Long getId() {return id; }
    public void setId(Long id) {this.id = id;}
       
}
