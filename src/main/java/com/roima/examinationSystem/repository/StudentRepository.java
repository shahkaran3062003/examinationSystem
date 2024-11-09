package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Student findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
