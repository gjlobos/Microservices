package com.dh.accountservice.domain.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @SequenceGenerator(name = "accountSequence",sequenceName = "accountSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSequence")
    private Long id;

    private String alias;

    private Double availableAmount;

    private String cvu;

    private String userId;

}
