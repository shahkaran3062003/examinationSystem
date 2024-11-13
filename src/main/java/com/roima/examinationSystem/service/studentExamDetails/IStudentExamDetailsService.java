package com.roima.examinationSystem.service.studentExamDetails;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.StudentExamDetails;

import java.util.List;

public interface IStudentExamDetailsService {

    List<StudentExamDetails> getAllStudentExamDetailsByStudentId(int studentId);
    StudentExamDetails getStudentExamDetailsByStudentIdAndExamId(int studentId,int examId) throws ResourceNotFoundException;
    List<StudentExamDetails> getAllStudentExamDetailsByExamId(int examId);
}
