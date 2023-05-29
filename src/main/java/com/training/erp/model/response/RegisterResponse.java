package com.training.erp.model.response;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private boolean isAccountNonLocked;
    private boolean isAccountVerified;
    private String accountType;

}
