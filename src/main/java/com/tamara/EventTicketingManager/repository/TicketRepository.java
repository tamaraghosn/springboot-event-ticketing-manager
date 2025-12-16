package com.tamara.EventTicketingManager.repository;

import com.tamara.EventTicketingManager.domain.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    // spring jpa will work this out
    int countByTicketTypeId(UUID ticketTypeId);

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);

}
