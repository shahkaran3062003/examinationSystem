package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College,Integer> {
    College findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
