package com.roima.examinationSystem.request;

import com.roima.examinationSystem.customValidators.ValidList;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Min(1)
    private int category_id;

    private String image;

    @NotNull
    @ValidList
    private List<String> options;


    public AddMcqQuestionRequest(String question, String difficulty, int correct_option, List<String> options,int category_id) {
        this.question = question;
        this.difficulty = difficulty;
        this.correct_option = correct_option;
        this.options = options;
        this.category_id = category_id;
    }
}
