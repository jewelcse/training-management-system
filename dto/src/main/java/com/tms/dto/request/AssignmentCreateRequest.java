package com.tms.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentCreateRequest {
    private String title;
    private double marks;
    private String filePath;
    private long courseId;
}
