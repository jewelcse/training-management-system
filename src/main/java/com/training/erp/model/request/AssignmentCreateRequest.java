package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentCreateRequest {
    private String title;
    private int marks;
    private String filePath;
    private long courseId;
}
