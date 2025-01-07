package com.roima.examinationSystem.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStudentProgrammingTestCaseAnswerDto {
    private int id;
    private Boolean status= false;
    private AdminProgrammingTestCaseDto programmingTestCase;
}
