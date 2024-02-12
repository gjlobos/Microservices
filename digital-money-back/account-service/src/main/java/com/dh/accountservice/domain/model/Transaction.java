package com.dh.accountservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    @Id
    @SequenceGenerator(name = "transactionSequence",sequenceName = "transactionSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionSequence")
    private Long id;

    @Column(name = "accountID")
    private Integer accountId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "dated")
    private String dated;

    @Column(name = "description")
    private String description;

    @Column(name = "destination")
    private String destination;

    @Column(name = "origin")
    private String origin;

    @Column(name = "type")
    private String type;

}
