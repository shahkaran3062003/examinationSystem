package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddMcqQuestionRequest {

    @NotNull
    @NotBlank
    private String question;

    @NotNull
    @NotBlank
    private String difficulty;

    @NotNull
    @Min(1)
    private int correct_option;



    @NotNull
    List<String> options;


    private String image;

}
