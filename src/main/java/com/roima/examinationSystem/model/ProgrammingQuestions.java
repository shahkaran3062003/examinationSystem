package com.roima.examinationSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
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


    @ManyToOne
    @JoinColumn(name="implementation_language")
    private Language implementationLanguage;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "programmingQuestions")
    private List<ProgrammingTestCase> programmingTestCase;


    @ManyToMany(mappedBy = "programmingQuestions")
    @JsonBackReference
    private List<Exam> exam;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.MERGE}, orphanRemoval = true, mappedBy = "programmingQuestions")
    @JsonBackReference
    private List<StudentProgrammingAnswer> studentProgrammingAnswer;

    public ProgrammingQuestions(String statement, Difficulty difficulty, String implementation, Language implementationLanguage,  Category category) {
        this.statement = statement;
        this.difficulty = difficulty;
        this.implementation = implementation;
        this.implementationLanguage = implementationLanguage;
        this.category = category;
    }
}
