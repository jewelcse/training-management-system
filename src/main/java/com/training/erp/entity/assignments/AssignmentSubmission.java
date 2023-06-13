package com.training.erp.entity.assignments;

import com.training.erp.entity.users.User;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "student_submissions")
@Builder
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User student;
    private String fileLocation;
    private double obtainedMarks;
    @ManyToOne
    private Assignment assignment;

}
