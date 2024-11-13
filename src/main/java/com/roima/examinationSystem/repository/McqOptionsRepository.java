package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.McqOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface McqOptionsRepository extends JpaRepository<McqOptions, Integer> {
}
