package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.model.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStudentProgrammingAnswerDto {
    private int id;

    private String submittedCode;

    private int totalPassTestCount=0;
    private int totalFailTestCount=0;

    private Boolean isSolved = false;

    private AdminProgrammingQuestionsDto programmingQuestions;

    private Language language;

    private List<AdminStudentProgrammingTestCaseAnswerDto> studentProgramTestCaseAnswer;
}
