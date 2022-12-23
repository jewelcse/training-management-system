package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToBatchRequest {
    private long batchId;
    private long userId;
}
