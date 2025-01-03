package com.roima.examinationSystem.dto.student;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubmittedMcqQuestionDto {

    private int id;
    private int selectedOption;
}
