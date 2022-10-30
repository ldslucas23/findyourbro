package com.findeyourbro.controller.notification;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.notification.Notification;

import com.findeyourbro.service.notification.NotificationService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/notification")
public class NotificationController {

    private NotificationService notificationService;
    
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
      
    @PostMapping("/add")
    @ApiOperation("Envia um convite para um usuário")
    public void sendNotificationAdd(@RequestBody Notification notification) {
        notificationService.sendNotification(notification);
    }
    
    @PostMapping("/accept")
    @ApiOperation("Rejeita um convite")
    public void acceptNotification(@RequestBody Notification notification) {
        notificationService.acceptNotification(notification);
    }
      
    @PostMapping("/reject")
    @ApiOperation("Rejeita um convite")
    public void rejectNotification(@RequestBody Notification notification) {
        notificationService.rejectNotification(notification);
    }
}
