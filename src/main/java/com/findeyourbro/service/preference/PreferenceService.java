package com.findeyourbro.service.preference;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
}
