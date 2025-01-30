package com.example.demo.controller;

import com.example.demo.DTO.BookingDTO;
import com.example.demo.service.RateLimiterService;
import com.example.demo.service.TicketBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final TicketBookingService bookingService;
    private final RateLimiterService rateLimiterService;

    @PostMapping("/book-ticket")
    public ResponseEntity<?> bookTicket(@RequestParam Long userId, @RequestParam Long ticketId){
        if (rateLimiterService.isRateLimited()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Please try again later");
        }

        BookingDTO result = bookingService.bookTicket(userId, ticketId);
        return ResponseEntity.status(201).body(result);
    }

}
