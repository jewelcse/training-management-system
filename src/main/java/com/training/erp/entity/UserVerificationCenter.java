package com.training.erp.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_verification_center")
public class UserVerificationCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="verification_code")
    private String verificationCode;

    @Column(name="expiry_date")
    private Timestamp expiryDate;

    @Column(name="max_limit")
    private int maxLimit;

    @OneToOne
    private User user;
}
