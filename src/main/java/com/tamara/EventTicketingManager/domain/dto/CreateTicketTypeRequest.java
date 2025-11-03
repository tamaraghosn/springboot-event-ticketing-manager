package com.tamara.EventTicketingManager.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequest {

    private  String name;
    private  Double price;
    private Integer totalAvailable;
    private  String description;
}
