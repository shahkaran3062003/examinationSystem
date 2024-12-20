package com.roima.examinationSystem.dto.student;

import com.roima.examinationSystem.dto.McqOptionsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentMcqQuestionDto {

    private int id;

    private String question;

    private String image;

    private List<McqOptionsDto> mcqOptions;
}
