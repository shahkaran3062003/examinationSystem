package com.roima.examinationSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private String instructions;


    private int totalMcqQuestions;
    private int totalProgrammingQuestions;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime start_datetime;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime end_datetime;

    private int passing_criteria;

    private int duration;

    @Enumerated(value = EnumType.STRING)
    Difficulty difficulty;



    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private College college;



    @OneToMany(cascade = CascadeType.ALL,mappedBy = "exam",orphanRemoval = true)
    private List<ExamCategoryDetails> examCategoryDetails;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exam_mcq_questions",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "mcq_questions_id")
    )
    @JsonBackReference
    private List<McqQuestions> mcqQuestions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exam_programming_questions",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "programming_questions_id")
    )
    @JsonBackReference
    private List<ProgrammingQuestions> programmingQuestions;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam", orphanRemoval = true)
    @JsonBackReference
    private List<StudentExamDetails> studentExamDetails;


    public Exam(String title,String description, String instructions, int totalMcqQuestions, int totalProgrammingQuestions, LocalDateTime start_datetime, LocalDateTime end_datetime,int duration, int passing_criteria,Difficulty difficulty, College college) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.totalMcqQuestions = totalMcqQuestions;
        this.totalProgrammingQuestions = totalProgrammingQuestions;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.duration = duration;
        this.passing_criteria = passing_criteria;
        this.difficulty = difficulty;
        this.college = college;
    }
}
