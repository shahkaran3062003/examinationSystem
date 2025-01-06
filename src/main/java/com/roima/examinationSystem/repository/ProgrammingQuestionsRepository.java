package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.model.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgrammingQuestionsRepository extends JpaRepository<ProgrammingQuestions, Integer> {
    List<ProgrammingQuestions> findAllByDifficulty(Difficulty difficulty);

    List<ProgrammingQuestions> findAllByCategory(Category Category);

    List<ProgrammingQuestions> findAllByDifficultyAndCategory(Difficulty difficulty, Category Category);

    int countAllByDifficultyAndCategory(Difficulty difficulty, Category Category);
    boolean existsById(int id);
}
