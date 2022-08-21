package com.training.erp.model.request;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDto {
    private String title;
    private int marks;
    private String file_path;
    private long course_id;
}
