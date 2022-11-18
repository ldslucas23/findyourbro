package com.findeyourbro.service.group;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.KeyGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.findeyourbro.model.group.Event;
import com.findeyourbro.model.response.StandardResponse;
import com.findeyourbro.model.user.User;
import com.findeyourbro.repository.UserRepository;
import com.findeyourbro.repository.group.EventRepository;
import com.findeyourbro.service.file.FileService;
import com.findeyourbro.service.user.UserService;

@Service
public class EventService {

    private final UserService userService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    
    public EventService(UserService userService, EventRepository eventRepository,
            UserRepository userRepository, FileService fileService) {
      this.userService = userService;
      this.eventRepository = eventRepository;
      this.userRepository = userRepository;
      this.fileService = fileService;
    }
    
    public StandardResponse createEvent(String authHeader, Event event){
        try {
            User user = userService.getUserByToken(authHeader);
            if(event.getLateLng() != null || !event.getLateLng().isEmpty()) {
                event.setLate(event.getLateLng().get(0));
                event.setLng(event.getLateLng().get(1));   
            }
            event.setOwnerId(user.getId());
            saveEventProfileImage(event);
            Event savedEvent = eventRepository.saveAndFlush(event);   
            user.addEvent(savedEvent);
            userRepository.save(user);
            return new StandardResponse(200, null);
        }catch(Exception e) {
            return new StandardResponse(500, null);
        }
    }
    
    private void saveEventProfileImage(Event event) {
        if(StringUtils.isNotBlank(event.getProfileImageBase64()) 
                && StringUtils.isNotBlank(event.getProfileImageName())){
            KeyGenerator keyGen = null;
            try {
                keyGen = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGen.init(256);
            event.setProfileImageKey(event.getProfileImageName()+keyGen.generateKey().toString());
            fileService.storeImage(event.getProfileImageName(), event.getProfileImageBase64(), event.getProfileImageKey());
        }  
    }

    public StandardResponse enterEvent(String authHeader, Long eventId) {
        try {
            User user = userService.getUserByToken(authHeader);
            Optional<Event> event = eventRepository.findById(eventId);
            if(!event.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado", new Throwable());
            }
            user.addEvent(event.get());
            userRepository.save(user);  
            return new StandardResponse(200, null);
        }catch(Exception e) {
            return new StandardResponse(500, null);
        }
    }
    
    public List<Event> findEventsByFilters(Double maxDistance, String atuhHeader) {  
        User loggedUser = userService.getLoggedUser(atuhHeader);
        return buildEvents(loggedUser, eventRepository.findAll(), maxDistance);
    }
    
    private List<Event> buildEvents(User loggedUser, List<Event> events, Double maxDistance) {
        List<Event> findedEvents = new ArrayList<>();
        double p1 = 0.0;
        double p2 = 0.0;
        double p3 = 0.0;
        double p4 = 0.0;
        double p5 = 0.0;
        double km = 0.0;
        if(loggedUser.getLate() != null && loggedUser.getLng() != null) {
            for(Event event : events) {
                  if(validateEventsFindeds(loggedUser, event)) {
                      // Inicio dos calculos 1° parte
                      p1 = Math.cos((90 - loggedUser.getLate()) * (Math.PI / 180));
                      // Inicio dos calculos 2° parte
                      p2 = Math.cos((90 - event.getLate()) * (Math.PI / 180));
                      // Inicio dos calculos 3° parte
                      p3 = Math.sin((90 - loggedUser.getLate()) * (Math.PI / 180));
                      // Inicio dos calculos 4° parte
                      p4 = Math.sin((90 - event.getLate()) * (Math.PI / 180));
                      // Inicio dos calculos 5° parte
                      p5 = Math.cos((loggedUser.getLng() - event.getLng()) * (Math.PI / 180));
                      km = ((Math.acos((p1 * p2) + (p3 * p4 * p5)) * 6371) * 1.15);
                      if (km <= maxDistance) {           
                       if(StringUtils.isNotBlank(event.getProfileImageKey())){
                           event.setPhoto(fileService.getImageAsUrl(event.getProfileImageKey()));   
                       } 
                       findedEvents.add(event);
                      }        
                   }                  
             }             
        }
        return findedEvents;
    }
    
    private boolean validateEventsFindeds(User loggedUser, Event event) {
        return (!loggedUser.getEvents().stream().filter(events -> events.getId() == event.getId()).findAny().isPresent()) 
          && event.getLate() != null && event.getLng() != null;
    }
}
