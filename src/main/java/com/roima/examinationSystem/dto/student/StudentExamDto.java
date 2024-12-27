package com.roima.examinationSystem.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime start_datetime;

    private LocalDateTime end_datetime;

    private int duration;

    private List<StudentMcqQuestionDto> mcqQuestions;

    private List<StudentProgrammingQuestionDto> programmingQuestions;

}
