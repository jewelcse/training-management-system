package com.training.erp.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;
    public LoginResponse(String jwt, String username, String email, List<String> roles) {
        this.token = jwt;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
