package com.roima.examinationSystem.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamDto {
    private int id;

    private String title;
    private String description;
    private String instructions;


    private int totalMcqQuestions;
    private int totalProgrammingQuestions;

    private Date start_datetime;

    private Date end_datetime;

    private int duration;

    private List<StudentMcqQuestionDto> mcqQuestions;

    private List<StudentProgrammingQuestionDto> programmingQuestions;

}
