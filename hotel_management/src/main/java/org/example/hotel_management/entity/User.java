package org.example.hotel_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hotel_management.enums.Role;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @Column(columnDefinition = "varchar(255)", unique = true, nullable = false, updatable = false)
    private String username;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(255)", unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(255)")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

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
