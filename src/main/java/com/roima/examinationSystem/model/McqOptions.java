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
public class McqOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int option_number;
    private String text;

    private String image;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private McqQuestions mcqQuestions;


    public McqOptions(int optionNumber, String text) {
        this.option_number = optionNumber;
        this.text = text;
    }

    public McqOptions(int option_number, String text, String image) {
        this.option_number = option_number;
        this.text = text;
        this.image = image;
    }
}
