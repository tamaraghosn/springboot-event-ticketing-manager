package com.tamara.EventTicketingManager.controller;


import com.tamara.EventTicketingManager.domain.dto.GetEventDetailsResponseDto;
import com.tamara.EventTicketingManager.domain.dto.GetPublishedEventDetailsResponseDto;
import com.tamara.EventTicketingManager.domain.dto.ListEventResponseDto;
import com.tamara.EventTicketingManager.domain.dto.ListPublishedEventResponseDto;
import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.mapper.EventMapper;
import com.tamara.EventTicketingManager.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private  final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents (
            @RequestParam(required = false) String q, // query is optional
            Pageable pageable){

        Page<Event> events;
        if (q != null && !q.trim().isEmpty()){
            events = eventService.searchPublishedEvents(q, pageable);
        } else {
            events =  eventService.listPublishedEvents(pageable);
        }

        return  ResponseEntity.ok(events.map(event -> eventMapper.toListPublishedEventResponseDto(event)));

    }
    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEvent(
            @PathVariable UUID eventId
    ){
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::totPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
