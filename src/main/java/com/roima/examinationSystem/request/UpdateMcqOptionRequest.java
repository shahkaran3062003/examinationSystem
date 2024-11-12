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
public class UpdateMcqOptionRequest {

    @NotNull
    @Min(value = 1,message = "Id must be greater than 0")
    private int id;

    @NotNull
    @NotBlank
    private String text;
}
