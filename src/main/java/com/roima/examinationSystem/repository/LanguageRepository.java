package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer> {

    boolean existsByName(String name);
    boolean existsByJudge0Id(int judge0Id);
}
