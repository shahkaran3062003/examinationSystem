package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.StudentExamDetails;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMcqAnswerRepository extends JpaRepository<StudentMcqAnswer,Integer> {
    StudentMcqAnswer findByStudentExamDetailsIdAndMcqQuestionsId(int studentExamDetailsId, int mcqQuestionsId);
    List<StudentMcqAnswer> findAllByStudentExamDetailsId(int studentExamDetailsId);
}
