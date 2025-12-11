package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotel_management.enums.ServiceCategory;

@Entity
@Table(name = "services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    private ServiceCategory category;

    private String description;
}
