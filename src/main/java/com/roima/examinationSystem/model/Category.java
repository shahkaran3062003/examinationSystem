package com.roima.examinationSystem.model;

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
public class  Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;


    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "category", fetch = FetchType.EAGER)
    private List<McqQuestions> mcqQuestions;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category",fetch = FetchType.EAGER)
    private List<ExamMcqCategory> examMcqCategory;
}
