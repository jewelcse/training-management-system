package com.training.erp.model.response;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseFullProfileResponse {
    private Course course;
    private List<Assignment> assignments;
}
