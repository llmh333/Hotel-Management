package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotel_management.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<BookingService> bookingServices;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
