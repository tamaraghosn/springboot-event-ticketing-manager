package com.tamara.EventTicketingManager.service.imp;

import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.entity.TicketType;
import com.tamara.EventTicketingManager.domain.entity.User;
import com.tamara.EventTicketingManager.domain.enums.EventStatusEnum;
import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.domain.requests.UpdateEventRequest;
import com.tamara.EventTicketingManager.domain.requests.UpdateTicketTypeRequest;
import com.tamara.EventTicketingManager.exception.EventNotFoundException;
import com.tamara.EventTicketingManager.exception.EventUpdateException;
import com.tamara.EventTicketingManager.exception.TicketTypeNotFoundException;
import com.tamara.EventTicketingManager.exception.UserNotFoundException;
import com.tamara.EventTicketingManager.repository.EventRepository;
import com.tamara.EventTicketingManager.repository.UserRepository;
import com.tamara.EventTicketingManager.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final UserRepository userRepository;

    private  final EventRepository eventRepository;


    @Override
    @Transactional
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

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
       return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);


    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest eventRequest) {

        //  Validate input IDs
        if (eventRequest.getId() == null) {
            throw new EventUpdateException("Event ID cannot be null");
        }

        if (!id.equals(eventRequest.getId())) {
            throw new EventUpdateException("Provided ID does not match path variable");
        }

        // Find existing event
        Event existingEvent = eventRepository.findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(() -> new EventNotFoundException(
                        "Event with ID '" + id + "' does not exist"
                ));

        // Update event basic fields
        existingEvent.setName(eventRequest.getName());
        existingEvent.setStart(eventRequest.getStart());
        existingEvent.setEnd(eventRequest.getEnd());
        existingEvent.setVenue(eventRequest.getVenue());
        existingEvent.setStatus(eventRequest.getStatus());
        existingEvent.setSalesStart(eventRequest.getSalesStart());
        existingEvent.setSalesEnd(eventRequest.getSalesEnd());

        // Prepare a set of ticket type IDs that exist in the request
        Set<UUID> requestTicketTypeIds = new HashSet<>();
        for (UpdateTicketTypeRequest ticketType : eventRequest.getTicketTypes()) {
            if (ticketType.getId() != null) {
                requestTicketTypeIds.add(ticketType.getId());
            }
        }

        // Remove ticket types that are not in the request anymore
        existingEvent.getTicketTypes().removeIf(
                existingTicket -> !requestTicketTypeIds.contains(existingTicket.getId())
        );

        // Map existing ticket types by their ID
        Map<UUID, TicketType> existingTicketTypes = new HashMap<>();
        for (TicketType ticketType : existingEvent.getTicketTypes()) {
            existingTicketTypes.put(ticketType.getId(), ticketType);
        }

        // Loop through the ticket types from the request
        for (UpdateTicketTypeRequest ticketTypeRequest : eventRequest.getTicketTypes()) {
            if (ticketTypeRequest.getId() == null) {

                // Create new ticket type
                TicketType newTicket = new TicketType();
                newTicket.setName(ticketTypeRequest.getName());
                newTicket.setPrice(ticketTypeRequest.getPrice());
                newTicket.setDescription(ticketTypeRequest.getDescription());
                newTicket.setTotalAvailable(ticketTypeRequest.getTotalAvailable());
                newTicket.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(newTicket);

            } else if (existingTicketTypes.containsKey(ticketTypeRequest.getId())) {

                // Update existing ticket type
                TicketType existingTicket = existingTicketTypes.get(ticketTypeRequest.getId());
                existingTicket.setName(ticketTypeRequest.getName());
                existingTicket.setPrice(ticketTypeRequest.getPrice());
                existingTicket.setDescription(ticketTypeRequest.getDescription());
                existingTicket.setTotalAvailable(ticketTypeRequest.getTotalAvailable());


            } else {
                // Ticket type not found
                throw new TicketTypeNotFoundException(
                        "Ticket type with ID '" + ticketTypeRequest.getId() + "' does not exist"
                );
            }
        }

        // Save and return updated event
        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID id) {

        eventRepository.findByIdAndOrganizerId(id, organizerId).ifPresent(event -> eventRepository.deleteById(event.getId()));
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);

    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepository.searchEvents(query, pageable);

    }

}
