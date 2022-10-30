package com.findeyourbro.controller.contact;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.user.User;
import com.findeyourbro.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/contact")
public class ContactController {

    private UserService userService;
    
    public ContactController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/find")
    @ApiOperation("Busca pessoas próximas com base nos filtros.")
    public List<User> findContactsByFilters(@RequestHeader("Authorization") String authHeader, @RequestParam Double maxDistance, @RequestParam Long interestId){
       return userService.findUsersByFilters(maxDistance, interestId, authHeader);       
    }
    
}
