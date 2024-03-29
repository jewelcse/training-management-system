package com.training.erp.model.response;

import com.training.erp.entity.users.Gender;
import com.training.erp.entity.users.Role;
import lombok.*;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfile {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String batch;
    private boolean isEnabled;
    private boolean isNonLocked;
    private Set<Role> roles;
    private String phoneNumber;
    private Gender gender;
    private Date dateOfBirth;
    private String address1;
    private String address2;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
