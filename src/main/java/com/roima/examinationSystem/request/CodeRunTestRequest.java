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
public class CodeRunTestRequest {

    @NotNull
    @Min(1)
    private int programmingQuestionsId;

    @NotNull
    @Min(1)
    private int languageId;

    @NotNull
    @NotBlank
    private String code;
}
