package com.tms.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private String topic;
    private String topic_description;
    private long course_id;
    private long batch_id;
    private Timestamp start_date;
    private Timestamp end_date;

}
