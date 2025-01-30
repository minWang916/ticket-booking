package com.example.demo.controller;

import com.example.demo.DTO.BookingDTO;
import com.example.demo.service.TicketBookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final TicketBookingService bookingService;

    BookingController(TicketBookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping("/book-ticket")
    public ResponseEntity<BookingDTO> bookTicket(@RequestParam Long userId, @RequestParam Long ticketId){
        BookingDTO result = bookingService.bookTicket(userId, ticketId);
        return ResponseEntity.status(201).body(result);
    }

}
