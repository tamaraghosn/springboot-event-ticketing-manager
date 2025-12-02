package com.tamara.EventTicketingManager.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private  final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents (Pageable pageable){

        Page<Event> events =  eventService.listPublishedEvents(pageable);
        return  ResponseEntity.ok(events.map(event -> eventMapper.toListPublishedEventResponseDto(event)));

    }

}
