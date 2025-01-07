package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProgrammingQuestionsDto {
    private int id;

    private String statement;

    private Difficulty difficulty;

    private String implementation;

    private Language implementationLanguage;

    private List<AdminProgrammingTestCaseDto> programmingTestCase;

    private Category category;
}
