package com.roima.examinationSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentProgrammingAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 2000)
    private String submittedCode;

    private int passTestCount;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exam exam;

    @ManyToOne
    private Student student;

    @ManyToOne
    private ProgrammingQuestions programmingQuestions;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "studentProgrammingAnswer")
    private List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswer;
}
