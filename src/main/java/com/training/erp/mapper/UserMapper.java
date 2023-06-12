package com.training.erp.mapper;

import com.training.erp.entity.batches.Batch;
import com.training.erp.entity.users.Profile;
import com.training.erp.entity.users.User;
import com.training.erp.model.response.UserDetails;
import com.training.erp.model.response.UserInfo;
import com.training.erp.model.response.UserProfile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
                .dateOfBirth(profile.getDob())
                .gender(profile.getGender())
                .country(profile.getCountry())
                .zipCode(profile.getZipCode())
                .state(profile.getState())
                .phoneNumber(profile.getPhone())
                .build();
    }

    public Set<UserDetails> usersToUserDetailsList(Set<User> users){
        Set<UserDetails> userDetailsList = new HashSet<>();
        users.forEach(user -> userDetailsList.add(userToUserDetails(user)));
        return userDetailsList;
    }

    public List<UserInfo> usersToUserInfoList(List<User> users) {
        List<UserInfo> userList = new ArrayList<>();
        users.forEach(user -> userList.add(userToUserInfo(user)));
        return userList;
    }

    public UserInfo userToUserInfo(User user){

//        AtomicReference<String> batch = new AtomicReference<>("N/A");
//        Optional<Batch> userBatch = Optional.ofNullable(user.getBatch());
//        userBatch.ifPresent(obj -> {
//            batch.set(userBatch.get().getBatchName());
//        });

        return UserInfo.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isNonLocked(user.isNonLocked())
                .isEnabled(user.isEnabled())
                .build();
    }

    public UserProfile userToUserProfile(User user){

        AtomicReference<String> batch = new AtomicReference<>("N/A");
        Optional<Batch> userBatch = Optional.ofNullable(user.getBatch());
        userBatch.ifPresent(obj -> {
            batch.set(userBatch.get().getBatchName());
        });

        return UserProfile
                .builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .isEnabled(user.isEnabled())
                .isNonLocked(user.isNonLocked())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .phoneNumber(user.getProfile().getPhone())
                .street(user.getProfile().getStreet())
                .state(user.getProfile().getState())
                .zipCode(user.getProfile().getZipCode())
                .gender(user.getProfile().getGender())
                .city(user.getProfile().getCity())
                .country(user.getProfile().getCountry())
                .dateOfBirth(user.getProfile().getDob())
                .address1(user.getProfile().getAddress1())
                .address2(user.getProfile().getAddress2())
                .roles(user.getRole())
                .batch(String.valueOf(batch))
                .build();
    }
}
