package com.training.erp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ManyToOne
    private User student;
    private String fileLocation;
    private double obtainedMarks;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

}
