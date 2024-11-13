package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.StudentMcqAnswer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMcqAnswerRepository extends JpaRepository<StudentMcqAnswer,Integer> {
    StudentMcqAnswer findByExamIdAndStudentIdAndMcqQuestionsId(int examId, int studentId, int mcqQuestionsId);

    List<StudentMcqAnswer> findAllByStudentId(int studentId);
}
