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
public class StudentProgrammingAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 2000)
    private String submittedCode;

    private int totalPassTestCount=0;
    private int totalFailTestCount=0;

    private boolean isSolved = false;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Exam exam;

    @ManyToOne
    @JsonBackReference
    private Student student;

    @ManyToOne
    private ProgrammingQuestions programmingQuestions;

    @ManyToOne
    private Language language;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "studentProgrammingAnswer")
    private List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswer;

    public StudentProgrammingAnswer(String submittedCode, Exam exam, Student student, ProgrammingQuestions programmingQuestions, Language language) {
        this.submittedCode = submittedCode;
        this.exam = exam;
        this.student = student;
        this.programmingQuestions = programmingQuestions;
        this.language = language;
    }


}
