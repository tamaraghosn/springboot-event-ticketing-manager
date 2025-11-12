package com.tamara.EventTicketingManager.mapper;


import com.tamara.EventTicketingManager.domain.dto.*;
import com.tamara.EventTicketingManager.domain.entity.Event;
import com.tamara.EventTicketingManager.domain.entity.TicketType;
import com.tamara.EventTicketingManager.domain.requests.CreateEventRequest;
import com.tamara.EventTicketingManager.domain.requests.CreateTicketTypeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper( componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventResponseDto toListEventResponseDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    GetEventTicketTypeDetailsResponseDto toGetEventTicketTypeDetailsResponseDto(TicketType ticketType);

}
