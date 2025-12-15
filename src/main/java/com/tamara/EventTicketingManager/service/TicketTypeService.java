package com.tamara.EventTicketingManager.service;

import com.tamara.EventTicketingManager.domain.entity.Ticket;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
