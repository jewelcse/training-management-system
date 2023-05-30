package com.training.erp.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.erp.entity.batches.Batch;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(max = 120)
    private String password;
    private boolean enabled;
    private boolean nonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> role = new HashSet<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Batch batch;


}
