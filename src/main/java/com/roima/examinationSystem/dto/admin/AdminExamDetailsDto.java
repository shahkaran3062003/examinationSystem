package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminExamDetailsDto {
    private int id;

    private String title;
    private String description;
    private String instructions;


    private int totalMcqQuestions;
    private int totalProgrammingQuestions;

    private LocalDateTime start_datetime;

    private LocalDateTime end_datetime;

    private int passing_criteria;

    private int duration;

    Difficulty difficulty;

    private CollegeDto college;
}
