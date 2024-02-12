package com.dh.userservice.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{

    @Id
    @SequenceGenerator(name = "userSequence",sequenceName = "userSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
    private String id;

    private String dni;

    private String email;

    private Boolean emailEnabled;

    private String firstName;

    private String lastName;

    private String password;

    private String phone;

    private Boolean deleted;

    public User(Long id, String firstName, String lastName, String email, String dni, String phone) {
    }
}
