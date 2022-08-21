package com.training.erp.model.response;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileResponse<T> {
    private User user;
    private T profile;
    private List<Batch> batches;
    private List<Course> courses;
}
