package com.tamara.EventTicketingManager.service;

import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface EventService {


    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
}
