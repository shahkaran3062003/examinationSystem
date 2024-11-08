package com.roima.examinationSystem.model;

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

    private boolean status= false;


    @ManyToOne
    private ProgrammingTestCase programmingTestCase;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudentProgrammingAnswer studentProgrammingAnswer;

}
