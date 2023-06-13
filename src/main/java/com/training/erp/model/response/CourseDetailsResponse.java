package com.training.erp.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseDetailsResponse {

    private long id;
    private String courseName;
    private String courseDescription;
    private UserInfo trainer;
    private List<AssignmentResponse> assignments;
}
