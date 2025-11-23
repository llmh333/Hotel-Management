package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    private Service service;

    private int quantity;

    private double totalPrice;
}
