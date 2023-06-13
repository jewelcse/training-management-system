package com.training.erp.model.response;

import com.training.erp.entity.users.Role;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo {
    private String username;
    private String email;
    private boolean isEnabled;
    private boolean isNonLocked;
}
