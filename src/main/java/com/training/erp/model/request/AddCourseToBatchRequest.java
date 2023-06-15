package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCourseToBatchRequest {
    private long batchId;
    private long courseId;
}
