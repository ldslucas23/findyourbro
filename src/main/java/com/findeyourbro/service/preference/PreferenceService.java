package com.findeyourbro.service.preference;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.findeyourbro.model.preference.Preference;
import com.findeyourbro.repository.PreferenceRepository;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    
    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public List<Preference> listAllPreferences() {
        return preferenceRepository.findAll();
    }
    
    @Transactional(rollbackFor = Exception.class) 
    public Preference savePreference(Preference preference) {
       preferenceRepository.save(preference);
       return preference;
    }
    
    public Preference getPreferenceById(Long preferenceId){
        Optional<Preference> preference = preferenceRepository.findById(preferenceId);
        
        if(!preference.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prefência não encontrada ao tentar adicionar usuário", null);
        }
        return preference.get();
    }
    
}
