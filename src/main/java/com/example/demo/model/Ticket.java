package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Boolean isBooked;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, unique = true)
    private String seat;
}
