package com.findeyourbro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{
        
    @Query("SELECT s FROM User s WHERE s.email = ?1 and s.password = ?2")
    Optional<User> findByEmailAndPassword(String email, String password);
    @Query("SELECT s FROM User s WHERE s.email = ?1")
    Optional<User> findByEmai(String email);
}

