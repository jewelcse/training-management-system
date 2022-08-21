package com.training.erp.model.request;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchRequestDto {
    private String batch_name;
    private String batch_description;
    private Timestamp start_date;
    private Timestamp end_date;
}
