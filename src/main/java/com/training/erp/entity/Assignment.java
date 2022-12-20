package com.training.erp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "marks")
    private int totalMarks;

    @Column(name = "file_location")
    private String fileLocation;


    @JsonBackReference
    @ManyToOne
    private User createdBy;

    @JsonBackReference
    @ManyToOne
    private Course course;

    @JsonBackReference
    @ManyToOne
    private Batch batch;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Set<AssignmentSubmission> submissions = new HashSet<>();

}
