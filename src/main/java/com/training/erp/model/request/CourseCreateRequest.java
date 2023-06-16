package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCreateRequest {
    private String courseName;
    private String courseDescription;
}
