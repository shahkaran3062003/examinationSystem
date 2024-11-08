package com.roima.examinationSystem.model;

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
    private Exam exam;

    @ManyToOne
    private Student student;

    @ManyToOne
    private McqQuestions mcqQuestions;
}
