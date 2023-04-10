package com.tms.entity;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "AssignmentSubmissions")
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    private String fileLocation;
    private double obtainedMarks;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

}
