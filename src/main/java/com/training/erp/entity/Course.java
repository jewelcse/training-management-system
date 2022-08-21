package com.training.erp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "courseName")
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseDescription;

    @JsonBackReference
    @ManyToOne
    private Trainer trainer;
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "courses",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    Set<Batch> batches;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    List<Assignment> Assignments;

}
