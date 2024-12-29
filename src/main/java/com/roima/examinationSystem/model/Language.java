package com.roima.examinationSystem.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roima.examinationSystem.repository.LanguageRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @Column(name = "judge0_id")
    int judge0Id;


    @OneToMany(mappedBy = "implementationLanguage")
    @JsonBackReference
    private List<ProgrammingQuestions> programmingQuestionsList;

    @OneToMany(mappedBy = "language")
    @JsonBackReference
    private List<StudentProgrammingAnswer> studentProgrammingAnswerList;

    public Language(String name, int judge0Id){
        this.name=name;
        this.judge0Id=judge0Id;
    }
}
