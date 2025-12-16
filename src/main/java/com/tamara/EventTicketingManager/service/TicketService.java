package com.tamara.EventTicketingManager.service;

import com.tamara.EventTicketingManager.domain.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketService {

    Page<Ticket> listTicketForUser(UUID userId, Pageable pageable);

}
