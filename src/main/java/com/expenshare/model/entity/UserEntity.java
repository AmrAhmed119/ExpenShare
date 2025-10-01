package com.expenshare.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "addr_line1", length = 150)
    private String addressLine1;

    @Column(name = "addr_line2", length = 150)
    private String addressLine2;

    @Column(name = "addr_city", length = 80)
    private String addressCity;

    @Column(name = "addr_state", length = 80)
    private String addressState;

    @Column(name = "addr_postal", length = 20)
    private String addressPostal;

    @Column(name = "addr_country", length = 2)
    private String addressCountry;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
