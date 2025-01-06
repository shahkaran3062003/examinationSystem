package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.Student;
import com.roima.examinationSystem.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddStudentRequest {


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
    @Min(1)
    @Max(5)
    private int year;

    @NotNull
    @Min(1)
    @Max(10)
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
    @NotBlank
    private String college_name;

    @NotNull
    @NotBlank
    private String college_email;

    private String college_address;

//    public AddStudentRequest(String name,String contact, BigInteger enrollment_number, int year, int semester, float cgpa, int backlog, String department , String fullName, String email, String password , int college_id) {
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

    public AddStudentRequest(String contact, String enrollment_number, int year, int semester, float cgpa, int backlog, String department , String fullName, String email, String password) {
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
    }

    public AddStudentRequest(String contact,String enrollment_number, int year, int semester, float cgpa, int backlog, String department, String fullName, String email, String password, String college_name, String college_email, String college_address){
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
        this.college_name = college_name;
        this.college_email = college_email;
        this.college_address = college_address;
    }


    public User getUser() {
        return new User(this.fullName, this.email, this.password, this.role);
    }

    public College getCollege() {
        return new College(this.college_name,this.college_email,this.college_address);
    }
    public Student getStudent(User user, College college) {
//        return new Student(this.name, this.contact, this.enrollment_number, this.year, this.semester, this.cgpa, this.backlog, this.department, user, college);
        return new Student(this.contact, this.enrollment_number, this.year, this.semester, this.cgpa, this.backlog, this.department, user, college);
    }


}
