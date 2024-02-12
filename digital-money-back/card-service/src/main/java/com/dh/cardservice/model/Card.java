package com.dh.cardservice.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Card")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {

    @Id
    @SequenceGenerator(name = "cardSequence",sequenceName = "cardSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cardSequence")
    private Long Id;

    @Column(name ="card_number")
    private String cardNumber;

    @Column(name ="security_code")
    private String securityCode;

    @Column(name ="expiration_date")
    private String expirationDate;

    @Column(name ="provider")
    private String provider;

    @Column(name ="name")
    private String name;

    @Column(name ="account_id")
    private Long accountId;


}
