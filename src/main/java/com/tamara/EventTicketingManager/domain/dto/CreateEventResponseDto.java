package com.tamara.EventTicketingManager.domain.dto;


import com.tamara.EventTicketingManager.domain.entity.TicketType;
import com.tamara.EventTicketingManager.domain.enums.EventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventResponseDto {

    private UUID id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    private EventStatusEnum status;
    private List<CreateTicketTypeResponseDto> ticketTypes = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
