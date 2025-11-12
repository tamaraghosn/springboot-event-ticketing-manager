package com.tamara.EventTicketingManager.controller;


import com.tamara.EventTicketingManager.domain.dto.CreateEventRequestDto;
import com.tamara.EventTicketingManager.domain.dto.CreateEventResponseDto;
import com.tamara.EventTicketingManager.domain.dto.GetEventDetailsResponseDto;
import com.tamara.EventTicketingManager.domain.dto.ListEventResponseDto;
import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.mapper.EventMapper;
import com.tamara.EventTicketingManager.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private  final EventMapper eventMapper;
    private  final EventService eventService;


    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {

        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID organizerId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(organizerId, createEventRequest);

        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return  new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEventsForOrganizer(
            @AuthenticationPrincipal Jwt jwt, Pageable pageable
    ){

        UUID organizerId = parseUserId(jwt);
       Page<Event> events =  eventService.listEventsForOrganizer(organizerId, pageable);
        return  ResponseEntity.ok(events.map(event -> eventMapper.toListEventResponseDto(event)));

    }


    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        UUID organizerId = parseUserId(jwt);
        return eventService.getEventForOrganizer(organizerId, eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    public  UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
    }


}
