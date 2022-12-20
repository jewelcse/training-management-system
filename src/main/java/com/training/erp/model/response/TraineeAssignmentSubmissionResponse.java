package com.training.erp.model.response;

import com.training.erp.entity.AssignmentSubmission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class TraineeAssignmentSubmissionResponse {
    private AssignmentSubmission assignment;
}
