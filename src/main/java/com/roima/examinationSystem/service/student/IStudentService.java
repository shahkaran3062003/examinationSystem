package com.roima.examinationSystem.service.student;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.request.AddStudentRequest;
import com.roima.examinationSystem.request.UpdateStudentRequest;

import java.util.List;

public interface IStudentService {

    List<Student> getAllStudents();
    Student getStudentById(int id) throws ResourceNotFoundException;
    void addStudent(AddStudentRequest student) throws ResourceNotFoundException, ResourceExistsException;
    void updateStudent(UpdateStudentRequest request, int id) throws ResourceNotFoundException, ResourceExistsException;
    void deleteStudentById(int id) throws ResourceNotFoundException;
    Student getStudentByEmail(String email) throws ResourceNotFoundException;


}
