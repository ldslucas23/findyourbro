package com.findeyourbro.service;

import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.findeyourbro.model.User;
import com.findeyourbro.repository.UserRepository;

@Service
public class UserService  implements UserDetailsService{

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional(rollbackFor = Exception.class) 
    public User saveUser(User user) { 
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); 
        user.setPassword(bCryptPasswordEncoder
               .encode(user.getPassword())); 
        userRepository.save(user);
        return user; 
    }
    
    public User getUserById(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id); 
        if (user == null) {
            throw new NotFoundException();
        }    
        return user.get();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmai(username); 
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }else {       
            user.get().setAccountNonLocked(true);
            user.get().setEnabled(true);
            user.get().setAccountNonExpired(true);
            user.get().setCredentialsNonExpired(true); 
        }      
        return user.get();
    }

    public User updateUser(Long id, User user) throws NotFoundException {
        Optional<User> userFinded = userRepository.findById(id); 
        if (userFinded == null) {
            throw new NotFoundException();
        }else {
            user.setId(id);
            userRepository.save(user);
        } 
        return user;
    }
       
}
