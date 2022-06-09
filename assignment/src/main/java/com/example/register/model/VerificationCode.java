package com.example.register.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class VerificationCode {
    //private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    private String otp;
    private Date expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id",referencedColumnName = "id")
    private User user;

}
