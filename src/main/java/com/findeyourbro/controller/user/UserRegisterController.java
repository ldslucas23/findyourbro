package com.findeyourbro.controller.user;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.User;
import com.findeyourbro.service.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/register")
public class UserRegisterController {
    
    private final UserService userService;
    
    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    @ApiOperation("Registra um usuário.")
    public User registerUser(@RequestBody @Valid User user) {
       return userService.saveUser(user);       
    }
    
}
