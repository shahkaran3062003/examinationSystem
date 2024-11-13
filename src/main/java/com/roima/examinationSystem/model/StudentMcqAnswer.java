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
public class StudentMcqAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int selectedOption;

    private boolean isCorrect = false;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Exam exam;

    @ManyToOne
    @JsonBackReference
    private Student student;

    @ManyToOne
    private McqQuestions mcqQuestions;

    public StudentMcqAnswer(int selectedOption, Exam exam, Student student, McqQuestions mcqQuestions) {
        this.selectedOption = selectedOption;
        this.exam = exam;
        this.student = student;
        this.mcqQuestions = mcqQuestions;
    }
}
