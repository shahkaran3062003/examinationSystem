package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.ProgrammingTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammingTestCaseRepository extends JpaRepository<ProgrammingTestCase,Integer> {

    List<ProgrammingTestCase> findAllByProgrammingQuestionsId(int programmingQuestionsId);
}
