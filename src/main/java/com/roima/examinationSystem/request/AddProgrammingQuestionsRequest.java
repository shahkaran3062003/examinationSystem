package com.roima.examinationSystem.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProgrammingQuestionsRequest {

    @NotNull
    @NotBlank
    @Size(max=2000, message = "Statement must not be more than 2000 characters")
    private String statement;


    @NotNull
    @NotBlank
    private String difficulty;

    @NotNull
    @NotBlank
    @Size(max=2000, message = "Implementation must not be more than 2000 characters")
    private String implementation;

    @NotNull
    @Min(1)
    private int implementationLanguageId;


    @NotNull
    @NotBlank
    private String category;


    List<AddProgrammingTestRequest> programmingTestCases;


    public AddProgrammingQuestionsRequest(String statement, String difficulty, String implementation, int implementationLanguageId, String category){
        this.statement = statement;
        this.difficulty = difficulty;
        this.implementation = implementation;
        this.implementationLanguageId = implementationLanguageId;
        this.category = category;
    }

}

