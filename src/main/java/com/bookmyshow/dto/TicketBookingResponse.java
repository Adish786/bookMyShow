package com.bookmyshow.dto;

import com.bookmyshow.model.Ticket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketBookingResponse {
    private String status;
    private String message;
    private Ticket ticket;
}
