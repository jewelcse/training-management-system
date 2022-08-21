package com.training.erp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
    private int marks;

    @Column(name = "file_path")
    private String filePath;


    @JsonBackReference
    @ManyToOne
    private Trainer trainer;

    @JsonBackReference
    @ManyToOne
    private Course course;

}
