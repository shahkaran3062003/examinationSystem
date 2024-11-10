package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class McqQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String question;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    private int correct_option;


    private String image;


    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "mcqQuestions", fetch = FetchType.EAGER,orphanRemoval = true)
    private List<McqOptions> mcqOptions;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mcqQuestions", orphanRemoval = true)
    @JsonBackReference
    private List<StudentMcqAnswer> studentMcqAnswer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Exam> exam;
}
