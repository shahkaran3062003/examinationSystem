package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 10)
    private String contact;

    @Column(nullable = false)
    private String enrollment_number;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Min(1)
    @Max(8)
    private int semester;

    @Column(nullable = false)
    @Max(10)
    private float cgpa;

    @Column(nullable = false)
    @Min(0)
    private int backlog;

    @Column(nullable = false)
    private String department;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "college_id")
    @JsonManagedReference
    private College college;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", orphanRemoval = true)
    @JsonBackReference
    private List<StudentExamDetails> studentExamDetails;


    public Student(String contact, String enrollmentNumber,int year, int semester,  float cgpa,  int backlog, String department, User user, College college) {

        this.contact = contact;
        this.enrollment_number = enrollmentNumber;
        this.year = year;
        this.semester = semester;
        this.cgpa = cgpa;
        this.backlog = backlog;
        this.department = department;
        this.user = user;
        this.college = college;
    }

    public User getUser(){
        if(this.user==null){
            this.user = new User();
            this.user.setRole(Role.STUDENT);
        }

        return this.user;
    }

    public College getCollege(){
        if(this.college==null){
            this.college= new College();
        }

        return this.college;
    }
}
