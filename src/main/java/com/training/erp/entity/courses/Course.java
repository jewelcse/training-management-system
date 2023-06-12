package com.training.erp.entity.courses;

import com.training.erp.entity.users.User;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "courses")
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseDescription;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private User trainer;

}
