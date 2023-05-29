package com.training.erp.entity;

import com.training.erp.entity.batches.Batch;
import com.training.erp.entity.courses.Course;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Timestamp start;
    private Timestamp end;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Batch batch;

}
