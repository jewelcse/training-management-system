package com.training.erp.model.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponseDto {
    private String title;
    private int marks;
    private String filePath;
    private long courseId;
    private long userId;
    private long batchId;
}
