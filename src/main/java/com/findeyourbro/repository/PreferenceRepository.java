package com.findeyourbro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.Preference;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference,Long>{
    
}
