package com.tamara.EventTicketingManager.service;

import com.tamara.EventTicketingManager.domain.dto.CreateEventRequest;
import com.tamara.EventTicketingManager.domain.entity.Event;

import java.util.UUID;

public interface EventService {


    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);

}
