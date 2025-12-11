package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotel_management.enums.RoomStatus;
import org.example.hotel_management.enums.RoomType;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private double pricePerHours;

    private String description;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
