package com.example.demo.service;

import com.example.demo.DTO.BookingDTO;
import com.example.demo.Exception.CustomException;
import com.example.demo.Mapper.BookingMapper;
import com.example.demo.model.*;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TicketBookingService {
    private final BookingRepository bookingRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    TicketBookingService(BookingRepository bookingRepository, TransactionRepository transactionRepository, TicketRepository ticketRepository, UserRepository userRepository){
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public BookingDTO bookTicket(Long userId, Long ticketId){

        // Query out user and ticket
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND.value()));

        Ticket ticket = ticketRepository.findByIdAndIsBookedFalseWithLock(ticketId)
                .orElseThrow(() -> new CustomException("Ticket already booked or not available", HttpStatus.NOT_FOUND.value()));

        // Ensure user has enough balance
        if(user.getBalance().compareTo(BigDecimal.valueOf(ticket.getPrice())) < 0){
            throw new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST.value());
        }

        // Deduct balance from user
        BigDecimal ticketPrice = BigDecimal.valueOf(ticket.getPrice());
        user.setBalance(user.getBalance().subtract(ticketPrice));
        userRepository.save(user);

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(ticketPrice);
        transaction.setTransactionType(TransactionType.DEBIT);
        transactionRepository.save(transaction);

        // Book the ticket
        ticket.setIsBooked(true);
        ticketRepository.save(ticket);

        // Record the booking
        Booking booking = new Booking();
        booking.setTicket(ticket);
        booking.setUser(user);
        bookingRepository.save(booking);

        return BookingMapper.INSTANCE.toDTO(booking);
    }
}
