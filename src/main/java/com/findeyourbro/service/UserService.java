package com.findeyourbro.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.KeyGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); 
        user.setPassword(bCryptPasswordEncoder
               .encode(user.getPassword())); 
        userRepository.save(user);
        return user; 
    }
    
    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id); 
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", new Throwable());
        }
        if(StringUtils.isNotBlank(user.get().getProfileImageKey())){
            user.get().setProfileImageBase64(fileService.getImageFileBase64(user.get().getProfileImageKey()));   
        }
        return user.get();
    }
    
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
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
    public User updateUser(Long id, User user) throws NotFoundException {
        Optional<User> userFinded = userRepository.findById(id); 
        if (!userFinded.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado", new UsernameNotFoundException(user.getEmail()));
        }else {
            saveUserProfileImage(user, userFinded.get());
            user.setId(id);
            userRepository.save(user);
        } 
        return user;
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
        }        
    }
       
}
