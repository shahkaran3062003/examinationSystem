package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.ExamMonitor;
import com.roima.examinationSystem.model.StudentExamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMonitorRepository extends JpaRepository<ExamMonitor,Integer> {
    List<ExamMonitor> findAllByStudentExamDetailsId(int studentExamDetailsId);

    @Query("SELECT em FROM ExamMonitor em INNER JOIN em.studentExamDetails sed WHERE sed.exam.id = :examId")
    List<ExamMonitor> findAllByExamId(int examId);
}
