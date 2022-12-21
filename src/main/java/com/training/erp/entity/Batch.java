package com.training.erp.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
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


    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "batch_users",
            joinColumns = @JoinColumn(
                    name = "batch_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            )
    )
    Set<User> users= new HashSet<>();



    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "batch_courses",
            joinColumns = @JoinColumn(
                    name = "batch_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"
            )
    )
    Set<Course> courses = new HashSet<>();





}
