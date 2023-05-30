package com.training.erp.entity.users;


import com.training.erp.entity.users.User;
import lombok.*;

import javax.persistence.*;
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

    private String otp;

    private Timestamp otpExpireAt;

    private int maxTries;

    @OneToOne
    private User user;
}
