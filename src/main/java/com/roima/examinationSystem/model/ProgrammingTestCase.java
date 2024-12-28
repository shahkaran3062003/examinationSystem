package com.roima.examinationSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Enumerated(EnumType.STRING)
    private TestCaseType type;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private ProgrammingQuestions programmingQuestions;

    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "programmingTestCase")
    @JsonBackReference
    private List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswer;

    public ProgrammingTestCase(String input, String output, ProgrammingQuestions programmingQuestions) {
        this.input = input;
        this.output = output;
        this.programmingQuestions = programmingQuestions;
    }

    public ProgrammingTestCase(String input, String output, ProgrammingQuestions programmingQuestions, TestCaseType type) {
        this.input = input;
        this.output = output;
        this.programmingQuestions = programmingQuestions;
        this.type = type;

    }


}

