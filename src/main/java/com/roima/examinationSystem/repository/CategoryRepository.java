package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    boolean existsByName(String name);
    Category findByName(String name);

    List<Category> findAllByQuestionType(QuestionType questionType);


}
