package com.training.erp.model.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequest {
    private Long id;
    public long getUserId() {
        return id;
    }
}
