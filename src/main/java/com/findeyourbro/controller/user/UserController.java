package com.findeyourbro.controller.user;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.findeyourbro.model.User;
import com.findeyourbro.service.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/user")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Busca o todas as informações do usuário pelo id.")
    public User getUser(@PathVariable Long id) throws NotFoundException {
       return userService.getUserById(id);       
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualiza as informações do usuário pelo id.")
    public User updateUser(@PathVariable Long id, @RequestBody User user) throws NotFoundException {
        return userService.updateUser(id, user);
    }
    
}
