package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;

    private String address;

    @Column(nullable = false,unique = true)
    private String email;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "college",orphanRemoval = true)
    @JsonBackReference
    private Set<Student> students;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "college",orphanRemoval = true)
    @JsonBackReference
    private List<Exam> exams;

    public College(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public College(String name, String email) {
        this.name = name;
        this.email = email;
        this.address="";
    }
}
