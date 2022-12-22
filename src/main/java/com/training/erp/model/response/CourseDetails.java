package com.training.erp.model.response;

import com.training.erp.entity.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseDetails {

    private long id;
    private String courseName;
    private String courseDescription;
    private UserDetails trainer;
    private List<AssignmentResponse> assignments;
}
