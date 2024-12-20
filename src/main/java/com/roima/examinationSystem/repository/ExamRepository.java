package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Integer> {

    List<Exam> findAllByDifficulty(Difficulty difficulty);

    List<Exam> findAllByCollegeId(int id);
}
