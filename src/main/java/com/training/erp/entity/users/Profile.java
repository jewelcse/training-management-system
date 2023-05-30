package com.training.erp.entity.users;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 10)
    private String firstName;
    @NotBlank
    @Size(max = 10)
    private String lastName;

    @Size(max = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Size(max = 100)
    private String address1;

    @Size(max = 100)
    private String address2;

    @Size(max = 100)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Size(max = 32)
    private String zipCode;

}
