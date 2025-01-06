package com.roima.examinationSystem.service.admin.studentManagement;

import com.roima.examinationSystem.dto.admin.StudentProgrammingAnswerDto;
import com.roima.examinationSystem.dto.student.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.StudentExamDetails;
import com.roima.examinationSystem.model.StudentMcqAnswer;
import com.roima.examinationSystem.model.StudentProgrammingAnswer;
import com.roima.examinationSystem.request.AddStudentMcqAnswerRequest;
import com.roima.examinationSystem.request.AddStudentProgrammingAnswerRequest;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;

import java.util.List;

public interface IStudentManagementService {

    //Student
    List<StudentDto> getAllStudents();
    List<StudentDto> getStudentsByCollegeId(int id);
    StudentDto getStudentById(int id) throws ResourceNotFoundException;
    void addStudent(AddStudentRequest student) throws ResourceNotFoundException, ResourceExistsException;
    void updateStudent(UpdateStudentRequest request, int id) throws ResourceNotFoundException, ResourceExistsException;
    void deleteStudentById(int id) throws ResourceNotFoundException;
    StudentDto getStudentByEmail(String email) throws ResourceNotFoundException;


    //Student Exam Details
    List<StudentExamDetails> getAllStudentExamDetailsByStudentId(int studentId);
    StudentExamDetails getStudentExamDetailsByStudentIdAndExamId(int studentId,int examId) throws ResourceNotFoundException;
    List<StudentExamDetails> getAllStudentExamDetailsByExamId(int examId);
    void deleteStudentExamDetailsById(int id) throws ResourceNotFoundException;

    //Student MCQ Answer
//    void addStudentMcqAnswer(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException;
    List<StudentMcqAnswer> getStudentMcqAnswerByStudentId(int studentExamDetailsId) throws ResourceNotFoundException;
    StudentMcqAnswer getStudentMcqAnswerById(int id) throws ResourceNotFoundException;
    void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException;

    //Student Programming Answer
//    void addStudentProgrammingAnswer(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException;
    List<StudentProgrammingAnswerDto> getAllProgrammingAnswerByStudentId(int studentExamDetails) throws ResourceNotFoundException;
    StudentProgrammingAnswerDto getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;
    void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;




}
