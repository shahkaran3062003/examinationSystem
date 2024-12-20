package com.roima.examinationSystem.dto.admin;

import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMcqQuestionDto {
    private int id;

    private String question;

    private Difficulty difficulty;

    private int correct_option;

    private String image;

    private Category category;

    private List<McqOptionsDto> mcqOptions;
}
