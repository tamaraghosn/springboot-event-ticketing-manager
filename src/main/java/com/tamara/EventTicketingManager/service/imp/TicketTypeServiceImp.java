package com.tamara.EventTicketingManager.service.imp;

import com.tamara.EventTicketingManager.domain.entity.Ticket;
import com.tamara.EventTicketingManager.domain.entity.TicketType;
import com.tamara.EventTicketingManager.domain.entity.User;
import com.tamara.EventTicketingManager.domain.enums.TicketStatusEnum;
import com.tamara.EventTicketingManager.exception.TicketSoldOutException;
import com.tamara.EventTicketingManager.exception.TicketTypeNotFoundException;
import com.tamara.EventTicketingManager.exception.UserNotFoundException;
import com.tamara.EventTicketingManager.repository.TicketRepository;
import com.tamara.EventTicketingManager.repository.TicketTypeRepository;
import com.tamara.EventTicketingManager.repository.UserRepository;
import com.tamara.EventTicketingManager.service.QrCodeService;
import com.tamara.EventTicketingManager.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TicketTypeServiceImp implements TicketTypeService {


    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        // look up the user
        User user =  userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(
                String.format("User with ID %s was not found", userId)
        ));

        // Get ticket type with pessimistic lock to handle concurrent access

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(()-> new TicketTypeNotFoundException(
                String.format("Ticket Type with ID %s was not found", ticketTypeId)
        ));


        // ticket is only created after the user purchases a selected ticket type so each ticket is created has a ticket type id assigned to it
       int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());

       Integer totalAvailable = ticketType.getTotalAvailable();

       if (purchasedTickets + 1 > totalAvailable){
           throw new TicketSoldOutException();
       }

       Ticket newTicket = new Ticket();
       newTicket.setStatus(TicketStatusEnum.PURCHASED);
       newTicket.setTicketType(ticketType);
       newTicket.setPurchaser(user);cm

       // we need a saved ticket to create the qr code bc the id of the qr code will be the same as the uuid generated for a saved ticket
       Ticket savedTicket = ticketRepository.save(newTicket);
      // setting up the link between the ticket and qr code
       qrCodeService.generateQrCode(savedTicket);

       return ticketRepository.save(savedTicket);

    }
}
