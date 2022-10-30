package com.findeyourbro.service.notification;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.notification.NotificationEnum;
import com.findeyourbro.service.user.UserService;

@Service
public class NotificationService {
    
    private UserService userService;
    
    NotificationService(UserService userService){
        this.userService = userService;
    }
    
    
    public void sendNotification(Notification notification) {
        if(NotificationEnum.SEND.equals(notification.getType())) {
            userService.sendNotification(notification);
        }
    }
    
    public void acceptNotification(Notification notification) {
        if(NotificationEnum.ACCEPTED.equals(notification.getType())) {
            userService.acceptNotification(notification);
        } 
    }
    
    public void rejectNotification(Notification notification) {
        if(NotificationEnum.REJECT.equals(notification.getType())) {
            userService.rejectNotification(notification);
        } 
    }
}
