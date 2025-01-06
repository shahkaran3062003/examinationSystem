package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {

//    @NotNull
//    @NotBlank
//    private String name;

    @NotNull
    @NotBlank
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contact;

    @NotNull
    private String enrollment_number;

    @NotNull
    private int year;

    @NotNull
    @Min(1)
    @Max(8)
    private int semester;

    @NotNull
    @Max(10)
    private float cgpa;

    @NotNull
    @Min(0)
    private int backlog;

    @NotNull
    @NotBlank
    private String department;

    private Role role = Role.STUDENT;


    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    private int college_id;


    public User getUser() {
        return new User(this.fullName, this.email, this.password, this.role);
    }


//    public UpdateStudentRequest(String name,String contact, BigInteger enrollment_number, int year, int semester, float cgpa, int backlog, String department , String fullName, String email, String password , int college_id) {
//        this.name = name;
//        this.contact = contact;
//        this.enrollment_number = enrollment_number;
//        this.year = year;
//        this.semester = semester;
//        this.cgpa = cgpa;
//        this.backlog = backlog;
//        this.department = department;
//        this.fullName = fullName;
//        this.email = email;
//        this.password = password;
//        this.college_id = college_id;
//    }

    public UpdateStudentRequest(String contact, String enrollment_number, int year, int semester, float cgpa, int backlog, String department , String fullName, String email, String password , int college_id) {
        this.contact = contact;
        this.enrollment_number = enrollment_number;
        this.year = year;
        this.semester = semester;
        this.cgpa = cgpa;
        this.backlog = backlog;
        this.department = department;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.college_id = college_id;
    }



}
