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
    private double marks;
    private String filePath;
    private String courseName;
}
