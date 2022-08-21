package com.training.erp.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String role;
}
