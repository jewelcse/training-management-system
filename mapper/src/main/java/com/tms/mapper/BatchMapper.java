package com.tms.mapper;

import com.tms.dto.response.*;
import com.tms.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchMapper {

    public BatchCreateResponse batchToBatchResponseDto(Batch batch){
        if (batch == null) return null;
        return BatchCreateResponse.builder()
                .id(batch.getId())
                .batchName(batch.getBatchName())
                .batchDescription(batch.getBatchDescription())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .build();
    }


    public List<BatchCreateResponse> batchListToBatchResponseDtoList(List<Batch> batches){
        List<BatchCreateResponse> responseDtoList = new ArrayList<>();
        batches.forEach(batch -> responseDtoList.add(batchToBatchResponseDto(batch)));
        return responseDtoList;
    }
}
