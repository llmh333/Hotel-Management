package org.example.hotel_management.entity;

import jakarta.persistence.*;
import javafx.scene.control.Tab;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OTPCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User userEmail;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean isUsed;

    @PrePersist
    private void prePersist(){
        this.isUsed = false;
    }

}
