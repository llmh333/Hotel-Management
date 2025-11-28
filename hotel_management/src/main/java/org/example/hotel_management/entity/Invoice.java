package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotel_management.enums.PaymentMethod;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Booking booking;

    private double totalRoomCharge;
    private double totalServiceCharge;

    private double totalAmount;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
