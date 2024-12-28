package com.roima.examinationSystem.request;


import com.roima.examinationSystem.customValidators.ValidBoolean;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProgrammingTestCaseRequest {


    @NotNull
    @Min(value = 1,message = "Id must be greater than 0")
    private int programmingQuestionId;

    @NotNull
    @NotBlank
    private String input;

    @NotNull
    @NotBlank
    private String output;

    @NotNull
    @NotBlank
    private String type;

}
