package com.training.erp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAddResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
