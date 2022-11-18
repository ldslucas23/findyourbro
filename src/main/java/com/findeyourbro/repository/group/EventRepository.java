package com.findeyourbro.repository.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findeyourbro.model.group.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>{

}
