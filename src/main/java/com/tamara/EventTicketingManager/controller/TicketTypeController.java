package com.tamara.EventTicketingManager.controller;


import com.tamara.EventTicketingManager.repository.TicketTypeRepository;
import com.tamara.EventTicketingManager.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private  final TicketTypeService ticketTypeService;

    @PostMapping("/{ticketTypeId}/tickets")
    public ResponseEntity<Void> purchaseTicket (
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketTypeId
            ){
        UUID attendeeId = parseUserId(jwt);
        ticketTypeService.purchaseTicket(attendeeId, ticketTypeId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public  UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
    }
}
