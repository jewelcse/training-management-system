package com.training.erp.model.response;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainer;
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
    private Trainer trainer;
    private List<Assignment> assignments;
}