package com.tamara.EventTicketingManager.controller;


import com.tamara.EventTicketingManager.domain.dto.CreateEventRequestDto;
import com.tamara.EventTicketingManager.domain.dto.CreateEventResponseDto;
import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.mapper.EventMapper;
import com.tamara.EventTicketingManager.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(params = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private  final EventMapper eventMapper;
    private  final EventService eventService;


    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {

        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID organizerId = UUID.fromString(jwt.getSubject());

        Event createdEvent = eventService.createEvent(organizerId, createEventRequest);

        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return  new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

    }


}
