package com.roima.examinationSystem.service.exam;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.CollegeRepository;
import com.roima.examinationSystem.repository.ExamRepository;
import com.roima.examinationSystem.repository.McqQuestionsRepository;
import com.roima.examinationSystem.repository.ProgrammingQuestionsRepository;
import com.roima.examinationSystem.request.AddDeleteExamMcqQuestionsRequest;
import com.roima.examinationSystem.request.AddDeleteExamProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.AddUpdateExamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ExamService implements IExamService {

    private final ExamRepository examRepository;
    private final CollegeRepository collegeRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;

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
}
