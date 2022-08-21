package com.training.erp.model.response;


import com.training.erp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoggedInUserProfileResponse<T> {
    private User user;
    private T profile;
}
