package com.bookmyshow.controller;

import com.bookmyshow.dto.TicketBookingRequest;
import com.bookmyshow.dto.TicketBookingResponse;
import com.bookmyshow.exception.ShowAndShowSeatAreNotRelatedException;
import com.bookmyshow.exception.ShowNotAvailableException;
import com.bookmyshow.exception.ShowSeatNotAvailableException;
import com.bookmyshow.model.Ticket;
import com.bookmyshow.service.TicketService;
import com.bookmyshow.validator.TicketRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TicketController {
    private final TicketService ticketService;
    private final TicketRequestValidator ticketRequestValidator;

    @Autowired
    public TicketController(TicketService ticketService, TicketRequestValidator ticketRequestValidator) {
        this.ticketService = ticketService;
        this.ticketRequestValidator = ticketRequestValidator;
    }

    public TicketBookingResponse bookTicket(TicketBookingRequest ticketBookingRequest) throws ShowNotAvailableException,
            ShowAndShowSeatAreNotRelatedException,
            ShowSeatNotAvailableException {

        return ticketRequestValidator.validate(ticketBookingRequest) ? callTicketService(ticketBookingRequest)
                : generateFailedResponse();
    }

    private TicketBookingResponse callTicketService(TicketBookingRequest ticketBookingRequest) throws ShowNotAvailableException,
            ShowAndShowSeatAreNotRelatedException,
            ShowSeatNotAvailableException {
        Ticket ticket = ticketService.bookTicket(ticketBookingRequest.getShowSeatIds(), ticketBookingRequest.getShowId());
        TicketBookingResponse successTicketResponse = new TicketBookingResponse();
        successTicketResponse.setStatus("success");
        successTicketResponse.setTicket(ticket);
        return successTicketResponse;
    }

    private TicketBookingResponse generateFailedResponse() {
        TicketBookingResponse ticketBookingResponse =  new TicketBookingResponse();
        ticketBookingResponse.setMessage("Failure, not valid inputs, please give showId or Provide Seats");
        ticketBookingResponse.setStatus("failure");
        return ticketBookingResponse;
    }
}
