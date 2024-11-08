package com.roima.examinationSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "college",orphanRemoval = true)
    private Set<Student> students;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "college",orphanRemoval = true)
    private List<Exam> exams;
}
