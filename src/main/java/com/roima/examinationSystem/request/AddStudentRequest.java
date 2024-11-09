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


    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contact;

    @NotNull
    private BigInteger enrollment_number;

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
    private String username;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @Min(1)
    private int college_id;


    public AddStudentRequest(String name,String contact, BigInteger enrollment_number, int year, int semester, float cgpa, int backlog, String department , String username, String email, String password , int college_id) {
        this.name = name;
        this.contact = contact;
        this.enrollment_number = enrollment_number;
        this.year = year;
        this.semester = semester;
        this.cgpa = cgpa;
        this.backlog = backlog;
        this.department = department;
        this.username = username;
        this.email = email;
        this.password = password;
        this.college_id = college_id;
    }

    public User getUser() {
        return new User(this.username, this.email, this.password, this.role);
    }

    public Student getStudent(User user, College college) {
        return new Student(this.name, this.contact, this.enrollment_number, this.year, this.semester, this.cgpa, this.backlog, this.department, user, college);
    }


}
