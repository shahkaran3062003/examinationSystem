package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.model.Student;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentMcqAnswerRequest {

    @NotNull
    @Min(1)
    private int selectedOption;

    @NotNull
    @Min(1)
    private int examId;

    @NotNull
    @Min(1)
    private int studentId;

    @NotNull
    @Min(1)
    private int mcqQuestionsId;
}
