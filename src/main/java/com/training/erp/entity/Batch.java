package com.training.erp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "batches", uniqueConstraints = {
        @UniqueConstraint(columnNames = "batchName")
})
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "batch")
    Set<Trainee> trainees;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    Set<Trainer> trainers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,

            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    Set<Course> courses;



}
