package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExamMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    private StudentExamDetails studentExamDetails;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime monitorTime;

    private String screenImage;
    private String userImage;



    public ExamMonitor(StudentExamDetails studentExamDetails, String screenImage, String userImage) {
        this.studentExamDetails = studentExamDetails;
        this.monitorTime = LocalDateTime.now();
        this.screenImage = screenImage;
        this.userImage = userImage;
    }

}
