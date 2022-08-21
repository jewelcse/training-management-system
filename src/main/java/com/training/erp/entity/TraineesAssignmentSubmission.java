package com.training.erp.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "traineesAssignmentSubmissions")
public class TraineesAssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assignment assignment;

    @ManyToOne
    private Trainee trainee;

    @ManyToOne
    private Course course;
    private String filePath;
    private int obtainedMarks;

}
