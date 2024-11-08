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
public class McqOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int option_number;
    private String text;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private McqQuestions mcqQuestions;


}
