package com.tamara.EventTicketingManager.domain;

public enum EventStatusEnum {

    DRAFT,      // Event is being created but not visible yet
    PUBLISHED,  // Event is live and users can buy tickets
    CANCELLED,  // Event was called off
    COMPLETED   // Event has finished
}
