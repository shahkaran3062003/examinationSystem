package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.ExamCategoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamCategoryDetailsRepository extends JpaRepository<ExamCategoryDetails,Integer> {
    boolean existsByCategoryAndDifficultyAndExam(Category category, Difficulty difficulty, Exam exam);
    List<ExamCategoryDetails> findAllByExam(Exam exam);
}
