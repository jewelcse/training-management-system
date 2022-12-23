package com.training.erp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "title")
    private String title;

    @Column(name = "marks")
    private double totalMarks;

    @Column(name = "file_location")
    private String fileLocation;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    @JoinColumn(name = "assignment_id")
    private Set<AssignmentSubmission> submissions = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
