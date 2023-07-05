package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveCourseRequest {
    private long batchId;
    private long courseId;
}
