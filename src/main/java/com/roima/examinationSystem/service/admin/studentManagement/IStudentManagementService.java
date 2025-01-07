package com.roima.examinationSystem.service.admin.studentManagement;

import com.roima.examinationSystem.dto.admin.AdminStudentExamDetailsDto;
import com.roima.examinationSystem.dto.admin.AdminStudentMcqAnswerDto;
import com.roima.examinationSystem.dto.admin.AdminStudentProgrammingAnswerDto;
import com.roima.examinationSystem.dto.student.StudentDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
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
    List<AdminStudentExamDetailsDto> getAllStudentExamDetailsByStudentId(int studentId);
    AdminStudentExamDetailsDto getStudentExamDetailsByStudentIdAndExamId(int studentId,int examId) throws ResourceNotFoundException;
    List<AdminStudentExamDetailsDto> getAllStudentExamDetailsByExamId(int examId);
    void deleteStudentExamDetailsById(int id) throws ResourceNotFoundException;

    //Student MCQ Answer
//    void addStudentMcqAnswer(AddStudentMcqAnswerRequest request) throws ResourceNotFoundException;
    List<AdminStudentMcqAnswerDto> getStudentMcqAnswerByStudentId(int studentExamDetailsId) throws ResourceNotFoundException;
    AdminStudentMcqAnswerDto getStudentMcqAnswerById(int id) throws ResourceNotFoundException;
    void deleteStudentMcqAnswerById(int id) throws ResourceNotFoundException;

    //Student Programming Answer
//    void addStudentProgrammingAnswer(AddStudentProgrammingAnswerRequest request) throws ResourceNotFoundException;
    List<AdminStudentProgrammingAnswerDto> getAllProgrammingAnswerByStudentId(int studentExamDetails) throws ResourceNotFoundException;
    AdminStudentProgrammingAnswerDto getProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;
    void deleteProgrammingAnswerById(int programmingAnswerId) throws ResourceNotFoundException;




}
