package com.training.erp.model.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveUserRequest {
    private long batchId;
    private long userId;
}
