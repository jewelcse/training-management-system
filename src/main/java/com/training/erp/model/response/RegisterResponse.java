package com.training.erp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private boolean isAccountLocked;
    private boolean isAccountVerified;
    private String profileType="TRAINEE ACCOUNT";

}
