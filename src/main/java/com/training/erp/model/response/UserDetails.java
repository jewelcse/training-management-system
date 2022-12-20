package com.training.erp.model.response;

import com.training.erp.entity.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetails {
    private String username;
    private String email;
    private boolean isEnabled;
    private boolean isLocked;
    private Set<Role> roles;
}
