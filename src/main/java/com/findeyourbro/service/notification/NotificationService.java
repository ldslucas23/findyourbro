package com.findeyourbro.service.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.response.StandardResponse;
import com.findeyourbro.repository.notification.NotificationRepository;
import com.findeyourbro.service.user.UserService;

@Service
public class NotificationService {
    
    private UserService userService;
    private NotificationRepository notificationRepository;
    
    NotificationService(UserService userService, NotificationRepository notificationRepository){
        this.userService = userService;
        this.notificationRepository = notificationRepository;
    }
    
    public Optional<Notification> findByIdAndRecipient(Long id, Long recipient){
        return notificationRepository.findByIdAndRecipient(id, recipient);
    } 
    
    public StandardResponse sendInviteNotification(Notification notification) { 
        return userService.sendNotification(notification);
    }
    
    public StandardResponse acceptNotification(Notification notification) {
       return userService.acceptNotification(notification);
    }
    
    public StandardResponse rejectNotification(Notification notification) {
       return userService.rejectNotification(notification);
    }
    
    public boolean isFindByOwnerAndRecipient(Long ownerId, Long recipientId){
        List<Notification> notifications = notificationRepository.findByOwnerAndRecipient(ownerId, recipientId);
        return notifications != null && !notifications.isEmpty();
    }
    
}
