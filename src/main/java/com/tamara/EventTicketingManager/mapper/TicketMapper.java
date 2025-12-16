package com.tamara.EventTicketingManager.mapper;


import com.tamara.EventTicketingManager.domain.dto.ListTicketResponseDto;
import com.tamara.EventTicketingManager.domain.dto.ListTicketTicketTypeResponseDto;
import com.tamara.EventTicketingManager.domain.entity.Ticket;
import com.tamara.EventTicketingManager.domain.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper( componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);
    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);


}
