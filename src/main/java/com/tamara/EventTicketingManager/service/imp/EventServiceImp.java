package com.tamara.EventTicketingManager.service.imp;

import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.entity.TicketType;
import com.tamara.EventTicketingManager.domain.entity.User;
import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.exception.UserNotFoundException;
import com.tamara.EventTicketingManager.repository.EventRepository;
import com.tamara.EventTicketingManager.repository.UserRepository;
import com.tamara.EventTicketingManager.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final UserRepository userRepository;

    private  final EventRepository eventRepository;


    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {
        User organizer = userRepository.findById(organizerId).orElseThrow( () -> new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));


        Event eventToCreate = new Event();

        List<TicketType> ticketTypesToCreate = eventRequest.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return  ticketTypeToCreate;
                }
        ).toList();


        eventToCreate.setName(eventRequest.getName());
        eventToCreate.setStart(eventRequest.getStart());
        eventToCreate.setEnd(eventRequest.getEnd());
        eventToCreate.setVenue(eventRequest.getVenue());
        eventToCreate.setSalesStart(eventRequest.getSalesStart());
        eventToCreate.setSalesEnd(eventRequest.getSalesEnd());
        eventToCreate.setStatus(eventRequest.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

         return  eventRepository.save(eventToCreate);
    }
}
