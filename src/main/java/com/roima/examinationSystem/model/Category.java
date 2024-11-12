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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;


    @Enumerated(value = EnumType.STRING)
    private QuestionType questionType;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "category", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<McqQuestions> mcqQuestions;

    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REMOVE},mappedBy = "category")
    @JsonBackReference
    private List<ProgrammingQuestions> programmingQuestions;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ExamCategoryDetails> examCategoryDetails;

    public Category(String name,QuestionType questionType) {
        this.name = name;
        this.questionType = questionType;
    }
}
