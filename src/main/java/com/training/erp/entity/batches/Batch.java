package com.training.erp.entity.batches;

import com.training.erp.entity.courses.Course;
import com.training.erp.entity.users.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchName;
    private String batchDescription;
    private Timestamp startDate;
    private Timestamp endDate;



    @OneToMany(mappedBy = "batch",
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<User> trainees= new ArrayList<>();



    @ManyToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "batches_courses",
            joinColumns = @JoinColumn(
                    name = "batch_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", referencedColumnName = "id"
            )
    )
    List<Course> courses = new ArrayList<>();





}
