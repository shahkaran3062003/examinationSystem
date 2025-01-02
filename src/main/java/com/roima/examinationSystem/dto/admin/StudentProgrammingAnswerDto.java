package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class StudentProgrammingAnswerDto {

    private int id;
    private String submittedCode;
    private int totalPassTestCount=0;
    private int totalFailTestCount=0;
    private Boolean isSolved = false;
    private ProgrammingQuestionDto programmingQuestions;
    private Language language;
    private List<StudentProgramTestCaseAnswer> studentProgramTestCaseAnswer;


    public StudentProgrammingAnswerDto(){
        this.programmingQuestions = new ProgrammingQuestionDto();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProgrammingQuestionDto{
        private int id;
        private String statement;
        private Difficulty difficulty;
        private String implementation;
        private Category category;
    }
}
