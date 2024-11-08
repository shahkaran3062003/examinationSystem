package com.roima.examinationSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigInteger enrollment_number;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false)
    private float cgpa;

    @Column(nullable = false)
    private int backlog;

    @Column(nullable = false)
    private String department;


    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "college_id")
    private College college;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", orphanRemoval = true)
    private List<StudentMcqAnswer> studentMcqAnswer;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", orphanRemoval = true)
    private List<StudentProgrammingAnswer> studentProgrammingAnswer;

}
