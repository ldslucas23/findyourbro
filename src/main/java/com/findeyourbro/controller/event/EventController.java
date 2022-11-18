package com.findeyourbro.controller.event;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.group.Event;
import com.findeyourbro.model.response.StandardResponse;
import com.findeyourbro.service.group.EventService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/event")
public class EventController {

    private final EventService eventService;
    
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    @GetMapping("/find")
    @ApiOperation("Busca eventos próximos com base nos filtros.")
    public List<Event> findContactsByFilters(@RequestHeader("Authorization") String authHeader, @RequestParam Double maxDistance){
       return eventService.findEventsByFilters(maxDistance, authHeader);       
    }
    
    @PostMapping
    @ApiOperation("Registra um evento.")
    public StandardResponse createEvent(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid Event event) {
       return eventService.createEvent(authHeader, event);       
    }
    
    @PostMapping("/enter/{eventId}")
    @ApiOperation("Entra em um grupo.")
    public StandardResponse enterEvent(@RequestHeader("Authorization") String authHeader, @PathVariable Long eventId) {
       return eventService.enterEvent(authHeader, eventId);       
    }    
    
}
