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
public class ProgrammingTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 2000)
    private String input;

    @Column(length = 2000)
    private String output;


    @ManyToOne
    private ProgrammingQuestions programmingQuestions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "programmingTestCase")
    private List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswer;
}

