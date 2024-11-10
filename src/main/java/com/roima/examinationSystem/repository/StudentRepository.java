package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Student findByUserEmail(String email);
    boolean existsByUserEmail(String email);

    List<Student> findAllByCollegeId(int id);
}
