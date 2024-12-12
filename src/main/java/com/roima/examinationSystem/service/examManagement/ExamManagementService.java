package com.roima.examinationSystem.service.examManagement;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.AddDeleteExamMcqQuestionsRequest;
import com.roima.examinationSystem.request.AddDeleteExamProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;
import com.roima.examinationSystem.request.AddUpdateExamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ExamManagementService implements IExamManagementService {

    private final ExamRepository examRepository;
    private final CollegeRepository collegeRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ExamCategoryDetailsRepository examCategoryDetailsRepository;
    private final CategoryRepository categoryRepository;


    //----------------------------------------Exam--------------------------------------------------------------------
    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public Exam getExamById(int id) throws ResourceNotFoundException {
        return examRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
    }

    @Override
    public List<Exam> getAllExamsByCollegeId(int id) throws ResourceNotFoundException {
        return examRepository.findAllByCollegeId(id);
    }

    @Override
    public List<Exam> getAllExamsByDifficulty(String difficulty) throws ResourceNotFoundException, InvalidValueException {
        try{
            Difficulty d = Difficulty.valueOf(difficulty);
            return examRepository.findAllByDifficulty(d);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid Difficulty!");
        }
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByExam(int examId) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        return exam.getMcqQuestions();
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestionsByExam(int examId) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        return exam.getProgrammingQuestions();
    }


    @Override
    public void addExam(AddUpdateExamRequest request) throws ResourceNotFoundException, InvalidValueException {

        try {
            College college = collegeRepository.findById(request.getCollege_id()).orElseThrow(() -> new ResourceNotFoundException("College not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if (request.getEnd_datetime().before(request.getStart_datetime()))
                throw new InvalidValueException("End date is before start date!");
            Exam exam = new Exam(
                    request.getTitle(),
                    request.getDescription(),
                    request.getInstructions(),
                    request.getTotalMcqQuestions(),
                    request.getTotalProgrammingQuestions(),
                    request.getStart_datetime(),
                    request.getEnd_datetime(),
                    request.getDuration(),
                    request.getPassing_criteria(),
                    difficulty,
                    college);

            examRepository.save(exam);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid Difficulty!");
        }
    }

    @Override
    public void addExamMcqQuestions(AddDeleteExamMcqQuestionsRequest request, int examId) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        for(Integer id: request.getMcqQuestionIds()){
            McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));
            exam.getMcqQuestions().add(mcqQuestions);
            examRepository.save(exam);
        }
    }

    @Override
    public void addProgrammingQuestions(AddDeleteExamProgrammingQuestionsRequest request, int examId) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        for(Integer id: request.getProgrammingQuestionIds()){
            ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));
            exam.getProgrammingQuestions().add(programmingQuestions);
            examRepository.save(exam);
        }
    }

    @Override
    public void updateExam(AddUpdateExamRequest request, int id) throws ResourceNotFoundException, InvalidValueException {

        try {
            Exam exam = examRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));
            College college = collegeRepository.findById(request.getCollege_id()).orElseThrow(() -> new ResourceNotFoundException("College not found!"));
            if (request.getEnd_datetime().before(request.getStart_datetime()))
                throw new InvalidValueException("End date is before start date!");

            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            exam.setTitle(request.getTitle());
            exam.setDescription(request.getDescription());
            exam.setInstructions(request.getInstructions());
            exam.setTotalMcqQuestions(request.getTotalMcqQuestions());
            exam.setTotalProgrammingQuestions(request.getTotalProgrammingQuestions());
            exam.setStart_datetime(request.getStart_datetime());
            exam.setEnd_datetime(request.getEnd_datetime());
            exam.setPassing_criteria(request.getPassing_criteria());
            exam.setDuration(request.getDuration());
            exam.setDifficulty(difficulty);
            exam.setCollege(college);

            examRepository.save(exam);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid Difficulty!");
        }
    }

    @Override
    public void deleteExam(int id) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        examRepository.delete(exam);
    }

    @Override
    public void deleteExamMcqQuestions(AddDeleteExamMcqQuestionsRequest request,int examId) throws ResourceNotFoundException {

        Exam exam   = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        int i=0;
        for(Integer id: request.getMcqQuestionIds()){
            if(mcqQuestionsRepository.existsById(id)){
                for(i=0;i<exam.getMcqQuestions().size();i++){
                    if(exam.getMcqQuestions().get(i).getId() == id){
                        exam.getMcqQuestions().remove(i);
                        examRepository.save(exam);
                        break;
                    }
                }
            }
            else{
                throw new ResourceNotFoundException("Mcq Question not found!");
            }
        }
    }

    @Override
    public void deleteExamProgrammingQuestions(AddDeleteExamProgrammingQuestionsRequest request, int examId) throws ResourceNotFoundException {

        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ResourceNotFoundException("Exam not found!"));
        int i=0;
        for(Integer id: request.getProgrammingQuestionIds()){
            if(mcqQuestionsRepository.existsById(id)){
                for(i=0;i<exam.getMcqQuestions().size();i++){
                    if(exam.getProgrammingQuestions().get(i).getId() == id){
                        exam.getProgrammingQuestions().remove(i);
                        examRepository.save(exam);
                        break;
                    }
                }
            }
            else{
                throw new ResourceNotFoundException("Programming Question not found!");
            }
        }
    }


    //------------------------------------------Exam Category Details----------------------------------------------------
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
