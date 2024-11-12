package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUpdateExamCategoryDetailsRequest    {

    @NotNull
    @NotBlank
    private String difficulty;

    @NotNull
    @Min(1)
    private int count;


    @NotNull
    @Min(1)
    private int categoryId;


    @NotNull
    @Min(1)
    private int examId;

}
