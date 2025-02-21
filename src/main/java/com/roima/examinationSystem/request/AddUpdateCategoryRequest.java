package com.roima.examinationSystem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUpdateCategoryRequest {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String questionType;
}
