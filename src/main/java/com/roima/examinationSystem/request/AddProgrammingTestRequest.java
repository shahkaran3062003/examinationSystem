package com.roima.examinationSystem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProgrammingTestRequest {
    @NotNull
    @NotNull
    private String input;

    @NotNull
    @NotBlank
    private String output;

    @NotNull
    @NotBlank
    private String type;
}
