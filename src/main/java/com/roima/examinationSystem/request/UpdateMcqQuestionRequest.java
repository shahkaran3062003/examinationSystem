package com.roima.examinationSystem.request;

import com.roima.examinationSystem.customValidators.ValidList;
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
public class UpdateMcqQuestionRequest {
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
    private List<UpdateMcqOptionRequest> options;


    public UpdateMcqQuestionRequest(String question, String difficulty, int correct_option, List<UpdateMcqOptionRequest> options,int category_id) {
        this.question = question;
        this.difficulty = difficulty;
        this.correct_option = correct_option;
        this.options = options;
        this.category_id = category_id;
    }
}
