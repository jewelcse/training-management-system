package com.training.erp.model.request;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchCreateRequest {
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;
}
