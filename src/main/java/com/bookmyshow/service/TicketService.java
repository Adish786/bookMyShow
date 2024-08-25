package com.bookmyshow.service;

import com.bookmyshow.exception.ShowAndShowSeatAreNotRelatedException;
import com.bookmyshow.exception.ShowNotAvailableException;
import com.bookmyshow.exception.ShowSeatNotAvailableException;
import com.bookmyshow.model.*;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.ShowSeatRepository;
import com.bookmyshow.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(ShowSeatRepository showSeatRepository, ShowRepository showRepository, TicketRepository ticketRepository) {
        this.showSeatRepository = showSeatRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(List<Long> showSeatIds, Long showId) throws ShowNotAvailableException,
            ShowAndShowSeatAreNotRelatedException,
            ShowSeatNotAvailableException {
        List<ShowSeat> showSeats = this.showSeatRepository.findAllById(showSeatIds);
        Optional<Show> optionalShow = showRepository.findById(showId);

        if (!optionalShow.isPresent()) {
            throw new ShowNotAvailableException("Show is not available");
        }

        //Validate if the showId and showseats belong to same showId!!
        //Check if all of them are available!!
        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new ShowSeatNotAvailableException("All seats are not available for booking at the moment, " +
                        "please choose another available seats");
            }

            if (showSeat.getShow().getId() != showId) {
                throw new ShowAndShowSeatAreNotRelatedException("Error!");
            }
        }

        //Lock these seats for the booking.
        for (ShowSeat showSeat : showSeats) {
            showSeat.setSeatStatus(SeatStatus.LOCKED);
            showSeatRepository.save(showSeat);
        }

        //Create a ticket with say Pending status
        Ticket ticket = new Ticket();
        ticket.setShowSeats(showSeats);
        ticket.setTicketStatus(TicketStatus.PENDING);
        ticket.setShow(optionalShow.get());
        ticket.setTimeofBooking(new Date());

        return ticketRepository.save(ticket);
    }
}
