package com.training.erp.mapper;

import com.training.erp.entity.users.Profile;
import com.training.erp.entity.users.User;
import com.training.erp.model.response.UserDetails;
import com.training.erp.model.response.UserProfile;
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

    public UserProfile profileToUserProfile(Profile profile){
        if (profile == null) return null;
        return UserProfile.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .address1(profile.getAddress1())
                .address2(profile.getAddress2())
                .city(profile.getCity())
                .street(profile.getStreet())
                .city(profile.getCity())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .country(profile.getCountry())
                .zipCode(profile.getZipCode())
                .state(profile.getState())
                .phoneNumber(profile.getPhoneNumber())
                .build();
    }

    public Set<UserDetails> usersToUserDetailsList(Set<User> users){
        Set<UserDetails> userDetailsList = new HashSet<>();
        users.forEach(user -> userDetailsList.add(userToUserDetails(user)));
        return userDetailsList;
    }
}
