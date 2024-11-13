package com.roima.examinationSystem.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddUpdateExamRequest {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String instructions;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss",timezone = "Asia/Kolkata")
    private Date start_datetime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss",timezone = "Asia/Kolkata")
    private Date end_datetime;

    @NotNull
    @Min(1)
    private int duration;

    @NotNull
    @Min(1)
    private int passing_criteria;

    @NotNull
    private int totalMcqQuestions;

    @NotNull
    private int totalProgrammingQuestions;

    @NotNull
    @NotBlank
    String difficulty;

    @NotNull
    @Min(1)
    private int college_id;
}
