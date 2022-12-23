package com.training.erp.model.response;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.AssignmentSubmission;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TraineeAssignmentResponse {
    private AssignmentResponse assignment;
    private List<AssignmentSubmissionResponse> submissions;
}
