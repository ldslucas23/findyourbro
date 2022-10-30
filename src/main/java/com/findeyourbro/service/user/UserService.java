package com.findeyourbro.service.user;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.KeyGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.findeyourbro.authentication.SecurityConstants;
import com.findeyourbro.model.contact.Contact;
import com.findeyourbro.model.notification.Notification;
import com.findeyourbro.model.notification.NotificationEnum;
import com.findeyourbro.model.user.User;
import com.findeyourbro.repository.UserRepository;
import com.findeyourbro.service.file.FileService;

@Service
public class UserService  implements UserDetailsService{

    private final UserRepository userRepository;
    private final FileService fileService;
    
    public UserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }
    
    @Transactional(rollbackFor = Exception.class) 
    public User saveUser(User user){
        Optional<User> userFinded = userRepository.findByEmai(user.getEmail()); 
        if (userFinded.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário já cadastrado", null);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); 
        user.setPassword(bCryptPasswordEncoder
               .encode(user.getPassword())); 
        userRepository.save(user);
        return user; 
    }
    
    public User getLoggedUser(String atuhHeader){
        Optional<User> user = userRepository.findById(getUserIdByToken(atuhHeader)); 
        
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", new Throwable());
        }        
        
        if(StringUtils.isNotBlank(user.get().getProfileImageKey())){
            user.get().setPhoto(fileService.getImageAsUrl(user.get().getProfileImageKey()));   
        }
        fillUserContacts(user.get().getContacts());
        
        user.get().setProfileImageBase64(null);
        user.get().setPassword(null);
        return user.get();
    }
    
    private void fillUserContacts(List<Contact> contacts) {
        for(Contact contact : contacts) {
            Optional<User> userContact = userRepository.findById(contact.getContactId()); 
            if(userContact.isPresent()) {
                if(StringUtils.isNotBlank(userContact.get().getProfileImageKey())){
                    userContact.get().setPhoto(fileService.getImageAsUrl(userContact.get().getProfileImageKey()));   
                }
                userContact.get().setContacts(null);
                userContact.get().setPassword(null);
                contact.setUser(userContact.get());
            }        
        }
    }
       
    @Override
    public User loadUserByUsername(String username){
        Optional<User> user = userRepository.findByEmai(username); 
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado",  new UsernameNotFoundException(username));
        }else {       
            user.get().setAccountNonLocked(true);
            user.get().setEnabled(true);
            user.get().setAccountNonExpired(true);
            user.get().setCredentialsNonExpired(true); 
        }      
        return user.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long id, User user, String authHeader){
        Optional<User> userFinded = userRepository.findById(id);
        Optional<User> duplicatedUser = userRepository.findByEmai(user.getEmail());
        if (!userFinded.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", new UsernameNotFoundException(user.getEmail()));
        }else if(duplicatedUser.isPresent() && duplicatedUser.get().getId() != userFinded.get().getId()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Esse e-mail já foi cadastrado por outro usuário", null);
        }else {
            verifyUserToken(authHeader, id);
            saveUserProfileImage(user, userFinded.get());
            user.setPassword(userFinded.get().getPassword());
            if(user.getLateLng() != null && !user.getLateLng().isEmpty()) {
                user.setLate(user.getLateLng().get(0));
                user.setLng(user.getLateLng().get(1));
            }
            user.setId(id);
            userRepository.save(user);
            if(StringUtils.isNotBlank(user.getProfileImageKey())){
                user.setPhoto(fileService.getImageAsUrl(user.getProfileImageKey()));   
            }
            user.setProfileImageBase64(null);
            user.setPassword(null);
        } 
        return user;
    }
    
    public List<User> findUsersByFilters(Double maxDistance, Long interestId, String atuhHeader) {  
        User loggedUser = getLoggedUser(atuhHeader);
        return buildUsersToContactList(loggedUser, userRepository.findAll(), maxDistance, interestId);
    }
    
    private List<User> buildUsersToContactList(User loggedUser, List<User> users, Double maxDistance, Long preferenceId) {
        List<User> findedUsers = new ArrayList<>();
        double p1 = 0.0;
        double p2 = 0.0;
        double p3 = 0.0;
        double p4 = 0.0;
        double p5 = 0.0;
        double km = 0.0;
        
        for(User user : users) {
            if(user.getId() != loggedUser.getId()) {
              if(validateUsersFindeds(loggedUser, user, preferenceId)) {
                  // Inicio dos calculos 1° parte
                  p1 = Math.cos((90 - loggedUser.getLate()) * (Math.PI / 180));
                  // Inicio dos calculos 2° parte
                  p2 = Math.cos((90 - user.getLate()) * (Math.PI / 180));
                  // Inicio dos calculos 3° parte
                  p3 = Math.sin((90 - loggedUser.getLate()) * (Math.PI / 180));
                  // Inicio dos calculos 4° parte
                  p4 = Math.sin((90 - user.getLate()) * (Math.PI / 180));
                  // Inicio dos calculos 5° parte
                  p5 = Math.cos((loggedUser.getLng() - user.getLng()) * (Math.PI / 180));
                  km = ((Math.acos((p1 * p2) + (p3 * p4 * p5)) * 6371) * 1.15);
                  if (km <= maxDistance) {
                   user.setPassword(null);
                   findedUsers.add(user);
                  }        
               }                  
             }
         }     
        return findedUsers;
    }
    
    private boolean validateUsersFindeds(User loggedUser, User userFinded, Long preferenceId) {
        return ((userFinded.getSports_interests().stream().filter(item1 -> {
            return loggedUser.getSports_interests().stream().filter(item2 -> item2.getId() == item2.getId()).findAny().isPresent();
        }).findFirst().isPresent() 
              || userFinded.getSports_interests().stream().filter(item2 -> item2.getId() == preferenceId).findAny().isPresent())) 
          && !loggedUser.getNotifications().stream().filter(notification -> notification.getOwner() == userFinded.getId() || 
                notification.getRecipient() == userFinded.getId()).findFirst().isPresent()
          && !userFinded.getNotifications().stream().filter(notification -> notification.getOwner() == loggedUser.getId() || 
                notification.getRecipient() == loggedUser.getId()).findFirst().isPresent()
          && (!loggedUser.getContacts().stream().filter(contact -> contact.getContactId() == loggedUser.getId() || 
                contact.getContactId() == loggedUser.getId()).findAny().isPresent());
        
    }
    
    
    private void saveUserProfileImage(User updatedUser, User oldUser) {
        if(StringUtils.isNotBlank(updatedUser.getProfileImageBase64()) 
                && StringUtils.isNotBlank(updatedUser.getProfileImageName())
                && !updatedUser.getProfileImageName().equals(oldUser.getProfileImageName())){
            KeyGenerator keyGen = null;
            try {
                keyGen = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGen.init(256);
            updatedUser.setProfileImageKey(updatedUser.getProfileImageName()+keyGen.generateKey().toString());
            if(StringUtils.isNotBlank(oldUser.getProfileImageKey())) {
                fileService.deleteImageByKey(oldUser.getProfileImageKey());   
            }
            fileService.storeImage(updatedUser.getProfileImageName(), updatedUser.getProfileImageBase64(), updatedUser.getProfileImageKey());
        }else {
            updatedUser.setProfileImageKey(oldUser.getProfileImageKey());
        }       
    }
    
    private Long getUserIdByToken(String atuhHeaderAuthorization) {
        String userIdByTokenString = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                .build()
                .verify(atuhHeaderAuthorization.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getSubject();
        Long userIdByToken = new Long(userIdByTokenString);
        return userIdByToken;
    }
    
    public void verifyUserToken(String atuhHeaderAuthorization, Long userId) {
        try {
            String userIdByTokenString = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                    .verify(atuhHeaderAuthorization.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            Long userIdByToken = new Long(userIdByTokenString);
            if(userIdByToken == null || (userIdByToken != null && !userIdByToken.equals(userId))){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário solicitado não corresponde ao usuário logado", null); 
            }
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário solicitado não corresponde ao usuário logado", null); 
        }
    }
    
    //Esse método é para quando alguém envia solicitação de amizade
    public void sendNotification(Notification notification) {       
        Optional<User> owner = userRepository.findById(notification.getOwner());
        if(!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remetente não encontrado", null);
        }
        notification.setType(NotificationEnum.PENDING);
        owner.get().addNotification(notification);
        userRepository.save(owner.get());
        
        Optional<User> recipient = userRepository.findById(notification.getRecipient());
        if(!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Desinatário não encontrado", null);
        }
        notification.setType(NotificationEnum.RECEIVED);
        recipient.get().addNotification(notification);
        userRepository.save(recipient.get());
        
    }
    
    //Esse método é para quando alguém aceita solicitação de amizade
    public void acceptNotification(Notification notification) {
        Optional<User> owner = userRepository.findById(notification.getOwner());
        if(!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remetente não encontrado", null);
        }
        
        Optional<User> recipient = userRepository.findById(notification.getRecipient());
        if(!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remetente não encontrado", null);
        }
        removeNotificationsWhenAcceptNotification(owner.get(), recipient.get(), notification);
        addContact(owner.get(), recipient.get());
        userRepository.save(owner.get());
        userRepository.save(recipient.get());
    }
    
    public void rejectNotification(Notification notification) {
        Optional<User> owner = userRepository.findById(notification.getOwner());
        if(!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remetente não encontrado", null);
        }

        Optional<User> recipient = userRepository.findById(notification.getRecipient());
        if(!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remetente não encontrado", null);
        }
        removeNotificationsWhenAcceptNotification(owner.get(), recipient.get(), notification);
        userRepository.save(owner.get());
        userRepository.save(recipient.get());
    } 
    
    private void removeNotificationsWhenAcceptNotification(User owner, User recipient, Notification notification) {      
        for(int notificationPos = 0; notificationPos < owner.getNotifications().size(); notificationPos++) {
            if(owner.getNotifications().get(notificationPos).getRecipient() == notification.getRecipient() 
                    && (owner.getNotifications().get(notificationPos).getType().equals(NotificationEnum.PENDING))){
                owner.getNotifications().remove(notificationPos);
                break;
            }
        }
        
        for(int notificationPos = 0; notificationPos < recipient.getNotifications().size(); notificationPos++) {
            if(recipient.getNotifications().get(notificationPos).getOwner() == notification.getOwner() 
                    && (recipient.getNotifications().get(notificationPos).getType().equals(NotificationEnum.RECEIVED))){
                recipient.getNotifications().remove(notificationPos);
                break;
            }
        }
        
    }
    
    private void addContact(User owner, User recipient) {
        Contact contact = new Contact();
        contact.setContactId(recipient.getId());
        owner.addContact(contact);     
        contact = new Contact();
        contact.setContactId(owner.getId());
        recipient.addContact(contact);
    }
    
}
