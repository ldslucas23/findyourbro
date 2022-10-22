package com.findeyourbro.service;

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
import com.findeyourbro.model.Preference;
import com.findeyourbro.model.User;
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
        user.get().setProfileImageBase64(null);
        user.get().setPassword(null);
        return user.get();
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
                for(Preference preference : user.getSports_interests()) {
                    if(preference.getId() == preferenceId) {
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
        }
        return findedUsers;
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
    
}
