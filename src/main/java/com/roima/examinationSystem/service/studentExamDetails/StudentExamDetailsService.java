package com.roima.examinationSystem.service.studentExamDetails;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.StudentExamDetails;
import com.roima.examinationSystem.model.StudentProgrammingAnswer;
import com.roima.examinationSystem.repository.StudentExamDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentExamDetailsService implements IStudentExamDetailsService {

    private final StudentExamDetailsRepository studentExamDetailsRepository;

    @Override
    public List<StudentExamDetails> getAllStudentExamDetailsByStudentId(int studentId) {
        return studentExamDetailsRepository.findAllByStudentId(studentId);
    }

    @Override
    public StudentExamDetails getStudentExamDetailsByStudentIdAndExamId(int studentId, int examId) throws ResourceNotFoundException {
        StudentExamDetails studentExamDetails = studentExamDetailsRepository.findByStudentIdAndExamId(studentId,examId);

        if(studentExamDetails==null){
            throw new ResourceNotFoundException("Student Exam Details not found!");
        }
        return studentExamDetails;

    }

    @Override
    public List<StudentExamDetails> getAllStudentExamDetailsByExamId(int examId) {
        return studentExamDetailsRepository.findAllByExamId(examId);
    }
}
