package com.training.erp.model.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private long id;
    private String courseName;
    private String courseDescription;
}
