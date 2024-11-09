package com.roima.examinationSystem.dto;

import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private int id;
    private String name;
    private String contact;
    private BigInteger enrollment_number;
    private int year;
    private int semester;
    private float cgpa;
    private int backlog;
    private String department;
    private User user;
    private CollegeDto college;
}
