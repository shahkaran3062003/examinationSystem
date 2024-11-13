package com.roima.examinationSystem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentProgrammingAnswerRequest {

    @NotNull
    @NotBlank
    private String submittedCode;

    @NotNull
    @Min(1)
    private int examId;
    @NotNull
    @Min(1)
    private int studentId;

    @NotNull
    @Min(1)
    private int programmingQuestionsId;
}
