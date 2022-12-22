package com.training.erp.mapper;

import com.training.erp.entity.User;
import com.training.erp.model.response.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    public UserDetails userToUserDetails(User user){
        if (user == null) return null;
        return UserDetails.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .isNonLocked(user.isNonLocked())
                .build();
    }

    public Set<UserDetails> usersToUserDetailsList(Set<User> users){
        Set<UserDetails> userDetailsList = new HashSet<>();
        users.forEach(user -> userDetailsList.add(userToUserDetails(user)));
        return userDetailsList;
    }
}
