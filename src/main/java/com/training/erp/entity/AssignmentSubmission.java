package com.training.erp.entity;

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
    private User user;
    private String fileLocation;
    private int obtainedMarks;
    @ManyToOne
    private Assignment assignment;

}
