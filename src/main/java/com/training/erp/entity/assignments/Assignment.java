package com.training.erp.entity.assignments;


import com.training.erp.entity.courses.Course;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private double totalMarks;
    private String fileLocation;
    @ManyToOne
    private Course course;

}
