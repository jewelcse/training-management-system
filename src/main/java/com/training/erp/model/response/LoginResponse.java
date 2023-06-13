package com.training.erp.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private List<String> roles;
}
