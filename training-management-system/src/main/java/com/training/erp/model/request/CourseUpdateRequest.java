package com.training.erp.model.request;


import com.training.erp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseUpdateRequest {
    private String course_name;
    private String course_description;
    private long course_id;
}
