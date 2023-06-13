package com.training.erp.model.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfo {
    private String courseName;
    private String courseDescription;
}
