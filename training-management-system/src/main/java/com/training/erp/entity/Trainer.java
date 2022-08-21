package com.training.erp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String designation;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "trainers",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<Batch> batches;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "trainer")
    private Set<Course> courses;
    @JsonBackReference
    @OneToOne
    private User user;


}
