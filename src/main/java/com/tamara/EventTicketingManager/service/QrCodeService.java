package com.tamara.EventTicketingManager.service;

import com.tamara.EventTicketingManager.domain.entity.QrCode;
import com.tamara.EventTicketingManager.domain.entity.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
