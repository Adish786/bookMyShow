package com.bookmyshow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketBookingRequest {
    private List<Long> showSeatIds;
    private Long showId;
}
