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


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST
    })
    Set<User> users= new HashSet<>();



    @ManyToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.PERSIST
    })
    List<Course> courses = new ArrayList<>();





}
