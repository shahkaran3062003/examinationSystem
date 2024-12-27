package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Date;

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
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exam exam;


    @ManyToOne
    @JsonBackReference
    private Student student;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime monitorTime;

    private String screenImage;
    private String userImage;



    public ExamMonitor(Exam exam, Student student, String screenImage, String userImage) {
        this.exam = exam;
        this.student = student;
        this.monitorTime = LocalDateTime.now();
        this.screenImage = screenImage;
        this.userImage = userImage;

        System.out.println("monitorTime" + this.monitorTime);
    }

}
