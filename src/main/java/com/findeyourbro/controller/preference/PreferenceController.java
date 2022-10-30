package com.findeyourbro.controller.preference;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findeyourbro.model.preference.Preference;
import com.findeyourbro.service.preference.PreferenceService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/auth/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;
    
    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }
    
    @GetMapping("/list")
    @ApiOperation("Busca o todas as preferências de esportes disponíveis para aplicar ao usuário.")
    public List<Preference> listAllPreferences(){
        return preferenceService.listAllPreferences();
    }
    
    @PostMapping
    @ApiOperation("Insere uma preferência de esporte para ficar disponível para aplicar ao usuário.")
    public Preference savePreference(@RequestBody Preference preference) {
        return preferenceService.savePreference(preference);
    }
}
