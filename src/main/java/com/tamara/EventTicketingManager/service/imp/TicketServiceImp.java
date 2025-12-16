package com.tamara.EventTicketingManager.service.imp;


import com.tamara.EventTicketingManager.domain.entity.Ticket;
import com.tamara.EventTicketingManager.repository.TicketRepository;
import com.tamara.EventTicketingManager.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImp  implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketForUser(UUID userId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(userId, pageable);
    }
}
