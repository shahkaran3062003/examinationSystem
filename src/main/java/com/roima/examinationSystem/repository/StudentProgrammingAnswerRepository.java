package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.StudentExamDetails;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import com.roima.examinationSystem.model.StudentProgrammingAnswer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentProgrammingAnswerRepository extends JpaRepository<StudentProgrammingAnswer,Integer> {

    List<StudentProgrammingAnswer> findAllByStudentExamDetailsId(int studentExamDetailsId);
    StudentProgrammingAnswer findByStudentExamDetailsIdAndProgrammingQuestionsId(int studentExamDetailsId,int programmingQuestionsId);
}
