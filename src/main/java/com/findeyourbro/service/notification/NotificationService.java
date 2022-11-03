package com.findeyourbro.service.notification;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.notification.NotificationEnum;
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
    
    public void sendInviteNotification(Notification notification) { 
        userService.sendNotification(notification);
    }
    
    public void acceptNotification(Notification notification) {
        userService.acceptNotification(notification);
    }
    
    public void rejectNotification(Notification notification) {
        userService.rejectNotification(notification);
    }
}
