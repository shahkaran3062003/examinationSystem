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
public class StudentExamDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private String examIp;

    @Column(nullable = true)
    private Boolean isStarted = false;
    private Boolean isSubmitted = false;
    private Boolean isPassed = false;

    private int totalCorrectMcqAnswers=0;
    private int totalWrongMcqAnswers=0;
    private int totalUnattemptedMcqQuestions=0;
    private int totalMcqQuestions=0;


    private int totalProgrammingQuestions=0;
    private int totalSolvedProgrammingQuestions=0;
    private int totalUnsolvedProgrammingQuestions=0;
    private int totalUnattemptedProgrammingQuestions=0;


    @ManyToOne
    @JsonBackReference
    private Student student;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Exam exam;

    @OneToMany(mappedBy = "studentExamDetails", cascade = CascadeType.ALL)
    private List<StudentMcqAnswer> studentMcqAnswers;

    @OneToMany(mappedBy = "studentExamDetails" , cascade = CascadeType.ALL)
    private List<StudentProgrammingAnswer> studentProgrammingAnswers;

    @OneToMany(mappedBy = "studentExamDetails", cascade = CascadeType.ALL)
    private List<ExamMonitor> examMonitors;


    public StudentExamDetails(String examIp, boolean isStarted ,int totalMcqQuestions, int totalUnattemptedMcqQuestions, int totalProgrammingQuestions, int totalUnattemptedProgrammingQuestions, Student student, Exam exam) {
        this.examIp = examIp;
        this.isStarted = isStarted;
        this.totalMcqQuestions = totalMcqQuestions;
        this.totalUnattemptedMcqQuestions = totalUnattemptedMcqQuestions;
        this.totalProgrammingQuestions = totalProgrammingQuestions;
        this.totalUnattemptedProgrammingQuestions = totalUnattemptedProgrammingQuestions;
        this.student = student;
        this.exam = exam;
    }

}
