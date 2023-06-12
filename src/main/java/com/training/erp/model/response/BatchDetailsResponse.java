package com.training.erp.model.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BatchDetailsResponse {
    private long id;
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<CourseInfo> courses;
    private List<UserInfo> trainees;
}
