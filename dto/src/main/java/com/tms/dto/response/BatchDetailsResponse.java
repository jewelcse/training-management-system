package com.tms.dto.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
    private List<CourseResponse> course;
    private Set<UserDetails> users;
}
