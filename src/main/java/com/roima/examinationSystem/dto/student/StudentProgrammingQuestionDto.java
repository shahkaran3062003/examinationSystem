package com.roima.examinationSystem.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgrammingQuestionDto {

    private int id;

    private String statement;

}
