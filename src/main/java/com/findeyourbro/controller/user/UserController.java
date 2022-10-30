package com.findeyourbro.controller.user;

import javax.validation.Valid;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.findeyourbro.model.user.User;
import com.findeyourbro.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/user")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("")
    @ApiOperation("Busca o todas as informações do usuário logado.")
    public User getUser(@RequestHeader("Authorization") String authHeader){
       return userService.getLoggedUser(authHeader);       
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualiza as informações do usuário pelo id.")
    public User updateUser(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @Valid @RequestBody User user) throws NotFoundException {
        return userService.updateUser(id, user, authHeader);
    }
    
}
