package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Integer> {

    List<Exam> findAllByDifficulty(Difficulty difficulty);

    List<Exam> findAllByCollegeId(int id);
}
