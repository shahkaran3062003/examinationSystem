package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.model.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface McqQuestionsRepository extends JpaRepository<McqQuestions,Integer> {

    List<McqQuestions> findAllByCategory(Category category);

    List<McqQuestions> findAllByDifficulty(Difficulty difficultyE);

    List<McqQuestions> findAllByDifficultyAndCategory(Difficulty difficultyE, Category category);

    int countAllByDifficultyAndCategory(Difficulty difficulty, Category category);

    List<McqQuestions> findAllByCategoryQuestionType(QuestionType questionType);

    boolean existsById(int id);
}
