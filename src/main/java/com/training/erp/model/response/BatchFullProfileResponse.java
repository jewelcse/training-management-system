package com.training.erp.model.response;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainee;
import com.training.erp.entity.Trainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchFullProfileResponse {
    private String batch_name;
    private List<Course> courses = new ArrayList<>();
    private List<Trainee> trainees = new ArrayList<>();
    private List<Trainer> trainers = new ArrayList<>();

}