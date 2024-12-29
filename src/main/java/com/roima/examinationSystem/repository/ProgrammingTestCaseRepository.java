package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.ProgrammingTestCase;
import com.roima.examinationSystem.model.TestCaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgrammingTestCaseRepository extends JpaRepository<ProgrammingTestCase,Integer> {

    List<ProgrammingTestCase> findAllByProgrammingQuestionsId(int programmingQuestionsId);

    int countByProgrammingQuestionsIdAndType(int programmingQuestionsId, TestCaseType type);
}
