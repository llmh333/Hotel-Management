package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
