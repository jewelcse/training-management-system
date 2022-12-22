package com.training.erp.model.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponse {
    private long id;
    private String title;
    private int marks;
    private String filePath;
    private String courseName;
    private String batchName;
}
