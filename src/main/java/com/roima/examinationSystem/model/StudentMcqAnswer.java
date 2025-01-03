package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentMcqAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int selectedOption;

    private boolean isCorrect = false;

    @ManyToOne
    @JsonBackReference
    private StudentExamDetails studentExamDetails;

    @ManyToOne
    private McqQuestions mcqQuestions;

    public StudentMcqAnswer(int selectedOption, StudentExamDetails studentExamDetails, McqQuestions mcqQuestions) {
        this.selectedOption = selectedOption;
        this.studentExamDetails = studentExamDetails;
        this.mcqQuestions = mcqQuestions;
    }
}
