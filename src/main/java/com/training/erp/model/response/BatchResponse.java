package com.training.erp.model.response;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BatchResponse {
    private long id;
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;
}
