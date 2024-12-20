package com.roima.examinationSystem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartExamRequest {
    @NotNull
    @Min(1)
    int examId;

    @NotNull
    @Min(1)
    int studentId;
}
