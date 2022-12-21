package com.training.erp.model.response;

import com.training.erp.entity.Course;
import com.training.erp.entity.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BatchDetailsDto {
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;
    private Set<Course> course;
    private Set<User> users;
}
