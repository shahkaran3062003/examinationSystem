package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.ExamMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMonitorRepository extends JpaRepository<ExamMonitor,Integer> {
    List<ExamMonitor> findAllByStudentIdAndExamId(int studentId, int examId);

    List<ExamMonitor> findAllByExamId(int examId);
}
