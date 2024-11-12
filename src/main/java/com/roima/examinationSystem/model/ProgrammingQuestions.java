package com.roima.examinationSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProgrammingQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 2000)
    private String statement;


    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @Column(length = 2000)
    private String implementation;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "programmingQuestions")
    private List<ProgrammingTestCase> programmingTestCase;


    @ManyToMany
    @JoinTable(
            name = "programming_questions_exams",
            joinColumns = @JoinColumn(name = "programming_questions_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    @JsonBackReference
    private List<Exam> exam;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.MERGE}, orphanRemoval = true, mappedBy = "programmingQuestions")
    @JsonBackReference
    private List<StudentProgrammingAnswer> studentProgrammingAnswer;

    public ProgrammingQuestions(String statement, Difficulty difficulty, String implementation, Category category) {
        this.statement = statement;
        this.difficulty = difficulty;
        this.implementation = implementation;
        this.category = category;
    }
}
