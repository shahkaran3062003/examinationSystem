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
public class StudentProgramTestCaseAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Boolean status= false;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProgrammingTestCase programmingTestCase;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private StudentProgrammingAnswer studentProgrammingAnswer;

    public StudentProgramTestCaseAnswer(Boolean status, ProgrammingTestCase testCase,StudentProgrammingAnswer studentProgrammingAnswer){
        this.status = status;
        this.programmingTestCase = testCase;
        this.studentProgrammingAnswer = studentProgrammingAnswer;
    }

}
