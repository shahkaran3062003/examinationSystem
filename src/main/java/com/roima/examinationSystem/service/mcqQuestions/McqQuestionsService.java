package com.roima.examinationSystem.service.mcqQuestions;

import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.InvalidNumberException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Category;
import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.McqOptions;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.repository.CategoryRepository;
import com.roima.examinationSystem.repository.McqOptionsRepository;
import com.roima.examinationSystem.request.AddMcqQuestionRequest;
import com.roima.examinationSystem.repository.McqQuestionsRepository;
import com.roima.examinationSystem.service.mcqOptions.McqOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class McqQuestionsService implements IMcqQuestionsService {

    private final McqQuestionsRepository mcqQuestionsRepository;
    private final CategoryRepository categoryRepository;
    private final McqOptionsService mcqOptionsService;
    private final McqOptionsRepository mcqOptionsRepository;

    @Override
    public void addMcqQuestions(AddMcqQuestionRequest request) throws ResourceNotFoundException, InvalidENUMException,InvalidNumberException {

        try{
            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if (request.getCorrect_option() > request.getOptions().size()) {
                throw new InvalidNumberException("Correct option is greater than the number of options!");
            }

            // TODO check image name already exists
            // TODO: save image to shared folder
            // TODO get image from multipart form data


            McqQuestions newMcqQuestion;
            if(request.getImage()==null) {

                newMcqQuestion  = new McqQuestions(
                        request.getQuestion(),
                        difficulty,
                        request.getCorrect_option(),
                        category
                );
            }else{
                newMcqQuestion  = new McqQuestions(
                        request.getQuestion(),
                        difficulty,
                        request.getCorrect_option(),
                        category,
                        request.getImage()
                );
            }
            mcqQuestionsRepository.save(newMcqQuestion);

            List<McqOptions> options = new ArrayList<>();
            int i=1;
            for(String text: request.getOptions()){
                McqOptions mcqOptions = mcqOptionsService.addMcqOptions(i,text);
                mcqOptions.setMcqQuestions(newMcqQuestion);
                mcqOptionsRepository.save(mcqOptions);
                // options.add(mcqOptionsService.addMcqOptions(i,text,newMcqQuestion));
                i++;
            }

            //newMcqQuestion.setMcqOptions(options);
            //mcqQuestionsRepository.save(newMcqQuestion);


        } catch (IllegalArgumentException e) {
            throw new InvalidENUMException("Invalid difficulty!");
        }

    }

    @Override
    public void updateMcqQuestions(AddMcqQuestionRequest request, int id) throws ResourceNotFoundException, InvalidENUMException,InvalidNumberException {


//        TODO add feature to update image
        try{
            McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));

            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if(request.getCorrect_option() > request.getOptions().size()) {
                throw new InvalidNumberException("Correct option is greater than the number of options!");
            }

            int i=0;
            for(McqOptions option: mcqQuestions.getMcqOptions()){
                option.setText(request.getOptions().get(i));
                i++;
            }

            mcqQuestions.setQuestion(request.getQuestion());
            mcqQuestions.setDifficulty(difficulty);
            mcqQuestions.setCategory(category);
            mcqQuestions.setCorrect_option(request.getCorrect_option());
            mcqQuestionsRepository.save(mcqQuestions);
        } catch (IllegalArgumentException e) {
            throw new InvalidENUMException("Invalid difficulty!");
        }

    }

    @Override
    public void deleteMcqQuestions(int id) throws ResourceNotFoundException {
        McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));
        mcqQuestionsRepository.delete(mcqQuestions);
    }

    @Override
    public List<McqQuestions> getAllMcqQuestions() {
        return mcqQuestionsRepository.findAll();
    }

    @Override
    public McqQuestions getMcqQuestionsById(int id) throws ResourceNotFoundException {
        return mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Questions not found!"));
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByCategory(int category_id) throws ResourceNotFoundException {

        Category category = categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
        return mcqQuestionsRepository.findAllByCategory(category);
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByDifficulty(String difficulty) throws InvalidENUMException {
        try {
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            return mcqQuestionsRepository.findAllByDifficulty(difficultyE);
        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        }
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidENUMException, ResourceNotFoundException {
        try{
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            Category category = categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            return mcqQuestionsRepository.findAllByDifficultyAndCategory(difficultyE,category);
        }catch(IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Override
    public List<McqQuestions> getRandomMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidENUMException, ResourceNotFoundException, InvalidNumberException {

        try {
            Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            int total_count = mcqQuestionsRepository.countAllByDifficultyAndCategory(difficultyE, category);

            if (number > total_count) {
                throw new InvalidNumberException("Number is greater than the total number of questions!");
            }

            List<McqQuestions> mcqQuestions = mcqQuestionsRepository.findAllByDifficultyAndCategory(difficultyE, category);

            Collections.shuffle(mcqQuestions);

            return mcqQuestions.subList(0, number);



        }catch (IllegalArgumentException e){
            throw new InvalidENUMException("Invalid difficulty!");
        }catch (IndexOutOfBoundsException e){
            throw new InvalidNumberException("Number is greater than the total number of questions!");
        }
    }
}
