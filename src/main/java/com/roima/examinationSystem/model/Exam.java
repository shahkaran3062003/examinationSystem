package com.roima.examinationSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Date start_datetime;
    private Date end_datetime;
    private int passing_criteria;



    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private College college;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "exam",orphanRemoval = true)
    private List<ExamCategoryDetails> examCategoryDetails;

    @ManyToMany(mappedBy = "exam")
    private List<McqQuestions> mcqQuestions;

    @ManyToMany(mappedBy = "exam")
    private List<ProgrammingQuestions> programmingQuestions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam", orphanRemoval = true)
    private List<StudentMcqAnswer> studentMcqAnswer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam", orphanRemoval = true)
    private List<StudentProgrammingAnswer> studentProgrammingAnswer;

}
