package com.roima.examinationSystem.service.questionManagement;

import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionManagementService implements IQuestionManagementService {

    // -----------------------------Category----------------------------------------
    private final CategoryRepository categoryRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final McqOptionsRepository mcqOptionsRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ProgrammingTestCaseRepository programmingTestCaseRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllCategoriesByQuestionType(String questionType) throws InvalidValueException {
        try{
            QuestionType questionTypeE = QuestionType.valueOf(questionType);
            return categoryRepository.findAllByQuestionType(questionTypeE);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }
    }

    @Override
    public Category getCategoryById(int id) throws ResourceNotFoundException {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void addCategory(AddUpdateCategoryRequest request) throws ResourceExistsException, InvalidValueException {

        try {
            boolean isPresentedName = categoryRepository.existsByName(request.getName());
            if (isPresentedName) {
                throw new ResourceExistsException("Category already exists!");
            }

            QuestionType questionType = QuestionType.valueOf(request.getQuestionType());

            Category category = new Category(request.getName(), questionType);
            categoryRepository.save(category);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }
    }

    @Override
    public void deleteCategoryById(int id) throws ResourceNotFoundException {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public void updateCategory(AddUpdateCategoryRequest request, int id) throws ResourceNotFoundException, ResourceExistsException, InvalidValueException {

        Category category = getCategoryById(id);

        if(!(category.getName().equals(request.getName())) && categoryRepository.existsByName(request.getName())){
            throw new ResourceExistsException("Category already exists!");
        }

        try{
            QuestionType questionType = QuestionType.valueOf(request.getQuestionType());
            category.setQuestionType(questionType);
            category.setName(request.getName());
            categoryRepository.save(category);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid question type!");
        }
    }


    // -----------------------------------------Mcq Questions--------------------------------------------

    @Override
    public void addMcqQuestions(AddMcqQuestionRequest request) throws ResourceNotFoundException, InvalidValueException {

        try{
            Category category = categoryRepository.findByName(request.getCategory());
            if(category==null){
                category = new Category(request.getCategory(), QuestionType.MCQ);
                categoryRepository.save(category);
            }
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if (request.getCorrect_option() > request.getOptions().size()) {
                throw new InvalidValueException("Correct option is greater than the number of options!");
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
                McqOptions mcqOptions = addMcqOptions(i,text);
                mcqOptions.setMcqQuestions(newMcqQuestion);
                mcqOptionsRepository.save(mcqOptions);
                // options.add(mcqOptionsService.addMcqOptions(i,text,newMcqQuestion));
                i++;
            }

            //newMcqQuestion.setMcqOptions(options);
            //mcqQuestionsRepository.save(newMcqQuestion);


        } catch (IllegalArgumentException e) {
            throw new InvalidValueException("Invalid difficulty!");
        }

    }

    @Override
    public void updateMcqQuestions(UpdateMcqQuestionRequest request, int id) throws ResourceNotFoundException, InvalidValueException {


//        TODO add feature to update image
        try{
            McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));

            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if(request.getCorrect_option() > request.getOptions().size()) {
                throw new InvalidValueException("Correct option is greater than the number of options!");
            }

            int i=0;
            for(UpdateMcqOptionRequest option: request.getOptions()){
                McqOptions options = mcqOptionsRepository.findById(option.getId()).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
                options.setText(option.getText());
                mcqOptionsRepository.save(options);
                i++;
            }

            mcqQuestions.setQuestion(request.getQuestion());
            mcqQuestions.setDifficulty(difficulty);
            mcqQuestions.setCategory(category);
            mcqQuestions.setCorrect_option(request.getCorrect_option());
            mcqQuestionsRepository.save(mcqQuestions);
        } catch (IllegalArgumentException e) {
            throw new InvalidValueException("Invalid difficulty!");
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
    public List<McqQuestions> getAllMcqQuestionsByCategoryQuestionType() {
        return mcqQuestionsRepository.findAllByCategoryQuestionType(QuestionType.MCQ);
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByDifficulty(String difficulty) throws InvalidValueException {
        try {
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            return mcqQuestionsRepository.findAllByDifficulty(difficultyE);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public List<McqQuestions> getAllMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidValueException, ResourceNotFoundException {
        try{
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            Category category = categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            return mcqQuestionsRepository.findAllByDifficultyAndCategory(difficultyE, category);
        }catch(IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public List<McqQuestions> getRandomMcqQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidValueException, ResourceNotFoundException {

        try {
            Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            int total_count = mcqQuestionsRepository.countAllByDifficultyAndCategory(difficultyE, category);

            if (number > total_count) {
                throw new InvalidValueException("Number is greater than the total number of questions!");
            }

            List<McqQuestions> mcqQuestions = mcqQuestionsRepository.findAllByDifficultyAndCategory(difficultyE, category);

            Collections.shuffle(mcqQuestions);

            return mcqQuestions.subList(0, number);



        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }catch (IndexOutOfBoundsException e){
            throw new InvalidValueException("Number is greater than the total number of questions!");
        }
    }


    // ----------------------------------------------------Mcq Options---------------------------------------------

    @Override
    public McqOptions addMcqOptions(int option_number, String text) {
        McqOptions mcqOption = new McqOptions(option_number,text);
        return mcqOptionsRepository.save(mcqOption);
    }

    @Override
    public void deleteMcqOptions(int id) throws ResourceNotFoundException {
        McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
        McqQuestions mcqQuestions = mcqoptions.getMcqQuestions();

        if(mcqQuestions.getCorrect_option() == mcqoptions.getOption_number()){
            throw new ResourceNotFoundException("Cannot delete correct option!");
        }

        mcqOptionsRepository.delete(mcqoptions);

    }

    @Override
    public void updateMcqOptions(int option_number, String text, int id) throws ResourceNotFoundException {
        McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
        mcqoptions.setOption_number(option_number);
        mcqoptions.setText(text);
        mcqOptionsRepository.save(mcqoptions);
    }


    // ----------------------------------------------Programming Questions-------------------------------------------

    @Override
    public void addProgrammingQuestions(AddProgrammingQuestionsRequest request) throws InvalidValueException, ResourceNotFoundException {
        try{
            Difficulty difficultyE = Difficulty.valueOf(request.getDifficulty());

            Category category = categoryRepository.findByName(request.getCategory());

            if(category == null){
                category = new Category(request.getCategory(), QuestionType.PROGRAMMING);
                categoryRepository.save(category);
            }

            ProgrammingQuestions programmingQuestions = new ProgrammingQuestions(request.getStatement(), difficultyE, request.getImplementation(),category);
            programmingQuestionsRepository.save(programmingQuestions);

            for(AddProgrammingTestRequest testCase: request.getProgrammingTestCases()){
                ProgrammingTestCase programmingTestCase = new ProgrammingTestCase(testCase.getInput(), testCase.getOutput(),programmingQuestions);
                programmingTestCaseRepository.save(programmingTestCase);
            }

        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public void updateProgrammingQuestions(UpdateProgrammingQuestionsRequest request, int id) throws ResourceNotFoundException, InvalidValueException {
        try{
            ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));

            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));

            for(UpdateProgrammingTestCaseRequest testCase: request.getProgrammingTestCases()){
                ProgrammingTestCase programmingTestCase = programmingTestCaseRepository.findById(testCase.getId()).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
                programmingTestCase.setInput(testCase.getInput());
                programmingTestCase.setOutput(testCase.getOutput());
                programmingTestCaseRepository.save(programmingTestCase);
            }

            programmingQuestions.setStatement(request.getStatement());
            programmingQuestions.setDifficulty(difficulty);
            programmingQuestions.setImplementation(request.getImplementation());
            programmingQuestions.setCategory(category);
            programmingQuestionsRepository.save(programmingQuestions);

        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public void deleteProgrammingQuestions(int id) throws ResourceNotFoundException {
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));
        programmingQuestionsRepository.delete(programmingQuestions);
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestions() {
        return programmingQuestionsRepository.findAll();
    }

    @Override
    public ProgrammingQuestions getProgrammingQuestionsById(int id) throws ResourceNotFoundException {
        return programmingQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestionsByCategory(int category_id) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
        return programmingQuestionsRepository.findAllByCategory(category);
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestionsByCategoryQuestionType() {
        return programmingQuestionsRepository.findAllByCategoryQuestionType(QuestionType.PROGRAMMING);
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficulty(String difficulty) throws InvalidValueException {

        try {
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            return programmingQuestionsRepository.findAllByDifficulty(difficultyE);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public List<ProgrammingQuestions> getAllProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id) throws InvalidValueException, ResourceNotFoundException {
        try{
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            Category category = categoryRepository.findById(category_id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            return programmingQuestionsRepository.findAllByDifficultyAndCategory(difficultyE, category);
        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }
    }

    @Override
    public List<ProgrammingQuestions> getRandomProgrammingQuestionsByDifficultyAndCategory(String difficulty, int category_id, int number) throws InvalidValueException, ResourceNotFoundException {
        try {
            Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
            Difficulty difficultyE = Difficulty.valueOf(difficulty);
            int total_count = programmingQuestionsRepository.countAllByDifficultyAndCategory(difficultyE, category);

            if (number > total_count) {
                throw new InvalidValueException("Number is greater than the total number of questions!");
            }

            List<ProgrammingQuestions> programmingQuestions = programmingQuestionsRepository.findAllByDifficultyAndCategory(difficultyE, category);

            Collections.shuffle(programmingQuestions);

            return programmingQuestions.subList(0, number);



        }catch (IllegalArgumentException e){
            throw new InvalidValueException("Invalid difficulty!");
        }catch (IndexOutOfBoundsException e){
            throw new InvalidValueException("Number is greater than the total number of programming questions!");
        }
    }


    // --------------------------------------------------Programming Test Case-----------------------------------------------

    @Override
    public ProgrammingTestCase getProgrammingTestCaseById(int id) throws ResourceNotFoundException {
        return programmingTestCaseRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Programming Test Case not found!"));
    }

    @Override
    public List<ProgrammingTestCase> getAllProgrammingTestCasesByProgrammingQuestionsId(int programmingQuestionsId) throws ResourceNotFoundException {
        if(!programmingQuestionsRepository.existsById(programmingQuestionsId)){
            throw new ResourceNotFoundException("Programming Questions not found!");
        }

        return programmingTestCaseRepository.findAllByProgrammingQuestionsId(programmingQuestionsId);
    }

    @Override
    public void addProgrammingTestCase(AddProgrammingTestCaseRequest request) throws ResourceNotFoundException {

        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionId()).orElseThrow(()-> new ResourceNotFoundException("Programming Questions not found!"));

        ProgrammingTestCase testCase = new ProgrammingTestCase(
                request.getInput(),
                request.getOutput(),
                programmingQuestions
        );
        programmingTestCaseRepository.save(testCase);
    }

    @Override
    public void updateProgrammingTestCase(UpdateProgrammingTestRequest request, int id) throws ResourceNotFoundException {
        ProgrammingTestCase testCase = programmingTestCaseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
        testCase.setInput(request.getInput());
        testCase.setOutput(request.getOutput());
        programmingTestCaseRepository.save(testCase);
    }

    @Override
    public void deleteProgrammingTestCase(int id) throws ResourceNotFoundException {
        ProgrammingTestCase testCase = programmingTestCaseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Programming Test Case not found!"));
        programmingTestCaseRepository.delete(testCase);

    }

}
