package com.findeyourbro.service.contact;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.notification.NotificationEnum;
import com.findeyourbro.model.response.StandardResponse;
import com.findeyourbro.model.user.User;
import com.findeyourbro.service.notification.NotificationService;
import com.findeyourbro.service.preference.PreferenceService;
import com.findeyourbro.service.user.UserService;

@Service
public class ContactService {

    private UserService userService;
    private NotificationService notificationService;
    private PreferenceService preferenceService;
    
    public ContactService(UserService userService, NotificationService notificationService, PreferenceService preferenceService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.preferenceService = preferenceService;
    }

    public StandardResponse inviteUser(String authHeader, Long id, Long preferenceId) {
        User owner = userService.getUserByToken(authHeader);   
        Notification newNotification = new Notification();     
        newNotification.setTitle("Solicitação de amizade");
        newNotification.setDescription(buildInviteUserMessage(owner.getName(), preferenceId));
        newNotification.setDatetime(java.sql.Date.valueOf(LocalDate.now()));
        newNotification.setOwner(owner.getId());
        newNotification.setRecipient(id);
        newNotification.setType(NotificationEnum.INVITE);
        return notificationService.sendInviteNotification(newNotification);       
    }
    
    private String buildInviteUserMessage(String userName, Long preferenceId) {
        StringBuilder message = new StringBuilder();
        
        message.append(userName);
        message.append(" se interessou em te convidar para praticar ");
        message.append(preferenceService.getPreferenceById(preferenceId).getName());
        message.append(", gostaria de adcionar ");
        message.append(userName);
        message.append(" a sua lista de contatos?");
        
        return message.toString();
    }
    
    public StandardResponse acceptUser(String authHeader, Long id, int accept) {
        User owner = userService.getUserByToken(authHeader);
        Optional<Notification> notification = notificationService.findByIdAndRecipient(id, owner.getId());
        if(notification.isPresent()) {
            if(accept == 1) {
                return notificationService.acceptNotification(notification.get());     
            }else {
                return notificationService.rejectNotification(notification.get());      
            }
        }
        return new StandardResponse(500, "Erro ao aceitar convite");
    }
    
}
