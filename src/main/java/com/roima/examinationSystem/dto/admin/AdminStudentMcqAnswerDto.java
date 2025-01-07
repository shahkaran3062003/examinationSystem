package com.roima.examinationSystem.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStudentMcqAnswerDto {

    private int id;

    private int selectedOption;

    private boolean isCorrect = false;

    private AdminMcqQuestionDto mcqQuestions;
}
