package com.roima.examinationSystem.repository;


import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.StudentExamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamDetailsRepository extends JpaRepository<StudentExamDetails,Integer> {
    StudentExamDetails findByStudentAndExam(Student student, Exam exam);

    List<StudentExamDetails> findAllByStudentId(int studentId);

    StudentExamDetails findByStudentIdAndExamId(int studentId, int examId);

    List<StudentExamDetails> findAllByExamId(int examId);
}
