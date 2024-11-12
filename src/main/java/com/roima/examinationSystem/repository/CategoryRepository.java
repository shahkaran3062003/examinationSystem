package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    boolean existsByName(String name);
    Category findByName(String name);
}
