package com.roima.examinationSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private boolean isStarted = false;
    private boolean isSubmitted = false;
    private boolean isPassed = false;

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

//    public StudentExamDetails(int totalMcqQuestions, int totalUnattemptedMcqQuestions, int totalProgrammingQuestions, int totalUnattemptedProgrammingQuestions, Student student, Exam exam) {
//        this.totalMcqQuestions = totalMcqQuestions;
//        this.totalUnattemptedMcqQuestions = totalUnattemptedMcqQuestions;
//        this.totalProgrammingQuestions = totalProgrammingQuestions;
//        this.totalUnattemptedProgrammingQuestions = totalUnattemptedProgrammingQuestions;
//        this.student = student;
//        this.exam = exam;
//    }
//
//    public StudentExamDetails(int totalProgrammingQuestions, int totalUnattemptedProgrammingQuestions, Student student, Exam exam) {
//        this.totalProgrammingQuestions = totalProgrammingQuestions;
//        this.totalUnattemptedProgrammingQuestions = totalUnattemptedProgrammingQuestions;
//        this.student = student;
//        this.exam = exam;
//    }

    public StudentExamDetails(boolean isStarted ,int totalMcqQuestions, int totalUnattemptedMcqQuestions, int totalProgrammingQuestions, int totalUnattemptedProgrammingQuestions, Student student, Exam exam) {
        this.isStarted = isStarted;
        this.totalMcqQuestions = totalMcqQuestions;
        this.totalUnattemptedMcqQuestions = totalUnattemptedMcqQuestions;
        this.totalProgrammingQuestions = totalProgrammingQuestions;
        this.totalUnattemptedProgrammingQuestions = totalUnattemptedProgrammingQuestions;
        this.student = student;
        this.exam = exam;
    }

}
