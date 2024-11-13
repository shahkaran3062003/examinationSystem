package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College,Integer> {
    College findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
