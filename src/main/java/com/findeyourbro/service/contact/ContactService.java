package com.findeyourbro.service.contact;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.notification.NotificationEnum;
import com.findeyourbro.model.user.User;
import com.findeyourbro.service.notification.NotificationService;
import com.findeyourbro.service.user.UserService;

@Service
public class ContactService {

    private UserService userService;
    private NotificationService notificationService;
    
    public ContactService(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public void inviteUser(String authHeader, Long id) {
        User owner = userService.getUserByToken(authHeader);
        Notification newNotification = new Notification();     
        newNotification.setTitle("Solicitação de amizade");
        newNotification.setDescription(owner.getName() + " gostaria de adicionar você como amigo.");
        newNotification.setDatetime(java.sql.Date.valueOf(LocalDate.now()));
        newNotification.setOwner(owner.getId());
        newNotification.setRecipient(id);
        newNotification.setType(NotificationEnum.INVITE);
        notificationService.sendInviteNotification(newNotification);       
    }
    
    public void acceptUser(String authHeader, Long id, int accept) {
        User owner = userService.getUserByToken(authHeader);
        Optional<Notification> notification = notificationService.findByIdAndRecipient(id, owner.getId());
        if(notification.isPresent()) {
            if(accept == 1) {
                notificationService.acceptNotification(notification.get());     
            }else {
                notificationService.rejectNotification(notification.get());      
            }
        }
    }
    
}
