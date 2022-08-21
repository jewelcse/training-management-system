package com.training.erp.model.response;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.TraineesAssignmentSubmission;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TraineeAssignmentResponse {
    private Assignment assignment;
    private List<TraineesAssignmentSubmission> submissions;
}
