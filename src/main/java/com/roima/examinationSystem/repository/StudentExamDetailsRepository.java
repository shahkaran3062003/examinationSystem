package com.roima.examinationSystem.repository;


import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.StudentExamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamDetailsRepository extends JpaRepository<StudentExamDetails,Integer> {
    StudentExamDetails findByStudentAndExam(Student student, Exam exam);

    List<StudentExamDetails> findAllByStudentId(int studentId);

    StudentExamDetails findByStudentIdAndExamId(int studentId, int examId);

    @Query("SELECT s FROM StudentExamDetails s WHERE s.exam.id = :examId ORDER BY s.isPassed DESC, s.totalCorrectMcqAnswers DESC, s.totalSolvedProgrammingQuestions DESC")
    List<StudentExamDetails> findExamResultsSorted(@Param("examId") int examId);
}
