package com.roima.examinationSystem.dto.student;

import com.roima.examinationSystem.model.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubmittedProgrammingQuestionDto {
    private int id;
    private String submittedCode;
    private int languageId;
}
