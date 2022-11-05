package com.findeyourbro.controller.contact;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.response.StandardResponse;
import com.findeyourbro.model.user.User;
import com.findeyourbro.service.contact.ContactService;
import com.findeyourbro.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/contact")
public class ContactController {

    private UserService userService;
    private ContactService contactService;
    
    public ContactController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }
    
    @GetMapping("/find")
    @ApiOperation("Busca pessoas próximas com base nos filtros.")
    public List<User> findContactsByFilters(@RequestHeader("Authorization") String authHeader, @RequestParam Double maxDistance, @RequestParam Long interestId){
       return userService.findUsersByFilters(maxDistance, interestId, authHeader);       
    }
    
    @PostMapping("/invite")
    @ApiOperation("Aceita um pedido de amizade para o contato")
    public StandardResponse accept(@RequestHeader("Authorization") String authHeader, @RequestParam Long id,  @RequestParam(required = false) Integer accept){
        return contactService.acceptUser(authHeader, id, accept == null ? 0 : accept);       
    }
    
    @PostMapping("/invite/{contactId}")
    @ApiOperation("Envia um pedido ou aceita um pedido de amizade para o contato")
    public StandardResponse inviteUser(@RequestHeader("Authorization") String authHeader, @PathVariable Long contactId){
       return contactService.inviteUser(authHeader, contactId);       
    }
    
}
