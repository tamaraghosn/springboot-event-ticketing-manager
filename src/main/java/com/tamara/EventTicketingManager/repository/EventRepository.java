package com.tamara.EventTicketingManager.repository;

import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.enums.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    void  deleteById(UUID id);

    Page<Event> findByStatus(EventStatusEnum statusEnum, Pageable pageable);


    // spring jpa wont be able to work out the query that we want so we need to make a custom one


    @Query(
            value = """
        SELECT * FROM events
        WHERE status = 'PUBLISHED'
        AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, ''))
            @@ plainto_tsquery('english', :searchTerm)
        """,
            countQuery = """
        SELECT count(*) FROM events
        WHERE status = 'PUBLISHED'
        AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, ''))
            @@ plainto_tsquery('english', :searchTerm)
        """,
            nativeQuery = true
    )
    Page<Event> searchEvents(@Param("searchTerm") String searchTerm, Pageable pageable);


}
