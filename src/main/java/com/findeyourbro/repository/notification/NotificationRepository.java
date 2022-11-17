package com.findeyourbro.repository.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.notification.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{

    @Query("SELECT s FROM Notification s WHERE s.id = ?1 and s.recipient = ?2")
    Optional<Notification> findByIdAndRecipient(Long id, Long recipient);
    
    @Query("SELECT s FROM Notification s WHERE s.owner = ?1 and s.recipient = ?2")
    List<Notification> findByOwnerAndRecipient(Long ownerId, Long recipientId);
}
