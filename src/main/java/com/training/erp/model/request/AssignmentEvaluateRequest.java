package com.training.erp.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentEvaluateRequest {
    private long submissionId;
    private int marks;
}
