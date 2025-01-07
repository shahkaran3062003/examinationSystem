package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStudentExamDetailsDto {
    private int id;
    private String examIp;


    private Boolean isStarted = false;
    private Boolean isSubmitted = false;
    private Boolean isPassed = false;

    private int totalCorrectMcqAnswers=0;
    private int totalWrongMcqAnswers=0;
    private int totalUnattemptedMcqQuestions=0;
    private int totalMcqQuestions=0;


    private int totalProgrammingQuestions=0;
    private int totalSolvedProgrammingQuestions=0;
    private int totalUnsolvedProgrammingQuestions=0;
    private int totalUnattemptedProgrammingQuestions=0;


    private List<AdminStudentMcqAnswerDto> studentMcqAnswers;

    private List<AdminStudentProgrammingAnswerDto> studentProgrammingAnswers;

    private List<AdminExamMonitorDto> examMonitors;
}
