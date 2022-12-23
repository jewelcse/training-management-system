package com.training.erp.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubmissionResponse {
    private Long submissionId;
    private String assignmentTitle;
    private String fileLocation;
    private String courseName;
    private double obtainedMarks;
    private double totalMarks;

}
