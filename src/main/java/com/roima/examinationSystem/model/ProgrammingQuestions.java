package com.roima.examinationSystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProgrammingQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 2000)
    private String statement;


    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @Column(length = 2000)
    private String implementation;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "programmingQuestions")
    private List<ProgrammingTestCase> programmingTestCase;


    @ManyToMany
    private List<Exam> exam;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true,mappedBy = "programmingQuestions")
    private List<StudentProgrammingAnswer> studentProgrammingAnswer;
}
