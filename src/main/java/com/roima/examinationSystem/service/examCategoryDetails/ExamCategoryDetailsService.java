package com.roima.examinationSystem.service.examCategoryDetails;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.ExamCategoryDetails;
import com.roima.examinationSystem.repository.CategoryRepository;
import com.roima.examinationSystem.repository.ExamCategoryDetailsRepository;
import com.roima.examinationSystem.repository.ExamRepository;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamCategoryDetailsService implements  IExamCategoryDetailsService{

    private final ExamCategoryDetailsRepository examCategoryDetailsRepository;
    private final CategoryRepository categoryRepository;
    private final ExamRepository examRepository;

    @Override
    public List<ExamCategoryDetails> getAllExamCategory() {
        return examCategoryDetailsRepository.findAll();
    }

    @Override
    public ExamCategoryDetails getExamCategoryById(int id) throws ResourceNotFoundException {
        return examCategoryDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public List<ExamCategoryDetails> getExamCategoryByExamId(int examId) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        return examCategoryDetailsRepository.findAllByExam(exam);
    }

    @Override
    public void addExamCategory(AddUpdateExamCategoryDetailsRequest request) throws ResourceNotFoundException, InvalidValueException {
        try{
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());
            Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
            if(examCategoryDetailsRepository.existsByCategoryAndDifficultyAndExam(category, difficulty, exam)) throw new InvalidValueException("Category with Same Difficulty already exists!");
            ExamCategoryDetails examCategoryDetails = new ExamCategoryDetails(difficulty, request.getCount(), category,exam);
            examCategoryDetailsRepository.save(examCategoryDetails);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid Difficulty!");
        }
    }

    @Override
    public void updateExamCategory(AddUpdateExamCategoryDetailsRequest request, int id) throws ResourceNotFoundException, InvalidValueException {
        try{
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());
            Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
            ExamCategoryDetails examCategoryDetails = examCategoryDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Exam Category Details not found!"));

            if(( (examCategoryDetails.getCategory().getId() != request.getCategoryId())
                    || !(examCategoryDetails.getDifficulty().toString().equals(difficulty.toString())
                    || (examCategoryDetails.getExam().getId() != request.getExamId()))
                    && examCategoryDetailsRepository.existsByCategoryAndDifficultyAndExam(category, difficulty, exam)
            )) throw new InvalidValueException("Category with Same Difficulty already exists!");


            examCategoryDetails.setCategory(category);
            examCategoryDetails.setDifficulty(difficulty);
            examCategoryDetails.setCount(request.getCount());
            examCategoryDetails.setExam(exam);
            examCategoryDetailsRepository.save(examCategoryDetails);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid Difficulty!");
        }
    }

    @Override
    public void deleteExamCategory(int id) throws ResourceNotFoundException {
        ExamCategoryDetails examCategoryDetails = examCategoryDetailsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Exam Category Details not found!"));
        examCategoryDetailsRepository.delete(examCategoryDetails);
    }
}
