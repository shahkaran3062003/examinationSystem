package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.StudentProgramTestCaseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentProgrammingTestCaseAnswerRepository  extends JpaRepository<StudentProgramTestCaseAnswer,Integer> {
}
