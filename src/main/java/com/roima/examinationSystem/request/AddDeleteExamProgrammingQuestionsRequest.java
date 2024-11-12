package com.roima.examinationSystem.request;


import com.roima.examinationSystem.customValidators.ValidList;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDeleteExamProgrammingQuestionsRequest {

    @NotNull
    @ValidList
    private List<Integer> programmingQuestionIds;
}
