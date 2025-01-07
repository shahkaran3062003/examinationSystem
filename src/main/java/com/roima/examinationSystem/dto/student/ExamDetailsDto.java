package com.roima.examinationSystem.dto.student;


import com.roima.examinationSystem.dto.CollegeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailsDto {

    private int id;

    private String title;
    private String description;
    private String instructions;

    private int totalMcqQuestions;
    private int totalProgrammingQuestions;

    private LocalDateTime start_datetime;

    private LocalDateTime end_datetime;

    private int duration;

    private CollegeDto college;
}
