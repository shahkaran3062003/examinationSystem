package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.dto.student.StudentExamDetailsDto;
import com.roima.examinationSystem.dto.student.StudentExamDto;
import com.roima.examinationSystem.exception.*;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.StartExamRequest;
import com.roima.examinationSystem.request.SubmitExamRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IStudentExamManagementService {

    List<StudentExamDetailsDto> getExamByCollegeId(int collegeId);

    StudentExamDto startExam(StartExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException;
    void submitExam(SubmitExamRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException;
    void submitMcqQuestion(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException;
    void submitProgrammingQuestion(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException, ResourceExistsException, ExamException, InvalidValueException, FetchException;

    void monitorExam(int studentId, int examId, MultipartFile userImage, MultipartFile screenImage) throws ResourceNotFoundException, InvalidValueException, IOException;
}
