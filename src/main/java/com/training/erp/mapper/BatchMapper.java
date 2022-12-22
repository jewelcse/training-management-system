package com.training.erp.mapper;

import com.training.erp.entity.Batch;
import com.training.erp.model.response.BatchResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchMapper {

    public BatchResponse batchToBatchResponseDto(Batch batch){
        if (batch == null) return null;
        return BatchResponse.builder()
                .id(batch.getId())
                .batchName(batch.getBatchName())
                .batchDescription(batch.getBatchDescription())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .build();
    }


    public List<BatchResponse> batchListToBatchResponseDtoList(List<Batch> batches){
        List<BatchResponse> responseDtoList = new ArrayList<>();
        batches.forEach(batch -> responseDtoList.add(batchToBatchResponseDto(batch)));
        return responseDtoList;
    }
}
