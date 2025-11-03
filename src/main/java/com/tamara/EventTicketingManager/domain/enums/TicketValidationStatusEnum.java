package com.tamara.EventTicketingManager.domain.enums;

public enum TicketValidationStatusEnum{

    VALID,      // Ticket is allowed to enter
    INVALID,    // Ticket isn’t valid (fake or reused)
    EXPIRED     // Ticket has passed its validity date/time

}
