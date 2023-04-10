package com.tms.dto.response;

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
    private UserDetails trainer;
    private List<AssignmentResponse> assignments;
}
