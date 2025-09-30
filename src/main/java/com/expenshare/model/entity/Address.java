package com.expenshare.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Column(name = "addr_line1", length = 150)
    private String line1;

    @Column(name = "addr_line2", length = 150)
    private String line2;

    @Column(name = "addr_city", length = 80)
    private String city;

    @Column(name = "addr_state", length = 80)
    private String state;

    @Column(name = "addr_postal", length = 20)
    private String postalCode;

    @Column(name = "addr_country", length = 2)
    private String country;
}