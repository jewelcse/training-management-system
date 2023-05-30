package com.training.erp.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchUpdateRequest {
    private long id;
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;
}
