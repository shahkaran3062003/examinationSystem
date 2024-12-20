package com.roima.examinationSystem.dto.student;


import com.roima.examinationSystem.dto.CollegeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamDetailsDto {

    private int id;

    private String title;
    private String description;
    private String instructions;

    private int totalMcqQuestions;
    private int totalProgrammingQuestions;

    private Date start_datetime;

    private Date end_datetime;

    private int duration;

    private CollegeDto college;
}
