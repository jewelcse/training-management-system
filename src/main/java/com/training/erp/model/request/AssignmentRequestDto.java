package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDto {
    private String title;
    private int marks;
    private String filePath;
    private long courseId;
    private long batchId;
}
