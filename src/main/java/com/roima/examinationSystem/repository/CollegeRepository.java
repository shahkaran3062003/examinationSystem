package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeRepository extends JpaRepository<College,Integer> {
    College findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
