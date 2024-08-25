package com.bookmyshow.validator;

import com.bookmyshow.dto.TicketBookingRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TicketRequestValidator {
    public boolean validate(TicketBookingRequest ticketBookingRequest) {
        return (ticketBookingRequest.getShowId() != null ||
                !CollectionUtils.isEmpty(ticketBookingRequest.getShowSeatIds()));
    }
}
