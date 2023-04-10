package com.tms.entity;


import lombok.*;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
