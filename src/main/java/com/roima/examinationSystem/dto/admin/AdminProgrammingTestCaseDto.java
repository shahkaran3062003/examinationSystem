package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.model.TestCaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProgrammingTestCaseDto {
    private int id;
    private String input;
    private String output;
    private TestCaseType type;
}
