package com.tamara.EventTicketingManager.controller;

import com.tamara.EventTicketingManager.domain.dto.ListEventResponseDto;
import com.tamara.EventTicketingManager.domain.dto.ListTicketResponseDto;
import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.entity.Ticket;
import com.tamara.EventTicketingManager.mapper.TicketMapper;
import com.tamara.EventTicketingManager.service.TicketService;
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
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public ResponseEntity<Page<ListTicketResponseDto>> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
            ){

        UUID purchaserId = parseUserId(jwt);
       Page <Ticket> tickets = ticketService.listTicketForUser(purchaserId, pageable);
        return ResponseEntity.ok(tickets.map(ticket -> ticketMapper.toListTicketResponseDto(ticket)));
    }

    public UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
    }
}
