package com.training.erp.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdatedSubmissionResponse {
    private Long submissionId;
    private double obtainedMarks;
    private double totalMarks;

}
