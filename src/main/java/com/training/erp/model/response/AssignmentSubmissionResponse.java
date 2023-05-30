package com.training.erp.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssignmentSubmissionResponse {
    private Long submissionId;
    private String assignmentTitle;
    private UserProfile student;
    private String fileLocation;
    private double obtainedMarks;
    private double totalMarks;

}
