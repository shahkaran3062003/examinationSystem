package com.roima.examinationSystem.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProgrammingTestRequest {

    @NotNull
    @NotBlank
    private String input;

    @NotNull
    @NotBlank
    private String output;
}
