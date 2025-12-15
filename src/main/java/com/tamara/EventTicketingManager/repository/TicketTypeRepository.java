package com.tamara.EventTicketingManager.repository;

import com.tamara.EventTicketingManager.domain.entity.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    // ensures that when one user is purchasing a ticket, other users must wait until the
    // transaction completes before they can access the same ticket type.

    @Query("SELECT tt FROM TicketType tt WHERE tt.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TicketType> findByIdWithLock(@Param("id") UUID id);

}
