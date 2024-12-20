package com.roima.examinationSystem.service.admin.questionManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.examinationSystem.dto.admin.AdminMcqQuestionDto;
import com.roima.examinationSystem.dto.McqOptionsDto;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.request.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
public class QuestionManagementService implements IQuestionManagementService {

    // -----------------------------Category----------------------------------------
    private final CategoryRepository categoryRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final McqOptionsRepository mcqOptionsRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ProgrammingTestCaseRepository programmingTestCaseRepository;
    private final ModelMapper modelMapper;


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

    @Transactional
    @Override
    public void addMcqQuestions(String requestJson, MultipartFile questionImage,List<MultipartFile> optionImages ) throws ResourceNotFoundException, InvalidValueException, IOException {

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            AddMcqQuestionRequest request = objectMapper.readValue(requestJson, AddMcqQuestionRequest.class);

            System.out.println(request);


            Category category = categoryRepository.findByName(request.getCategory());
            if(category==null){
                category = new Category(request.getCategory(), QuestionType.MCQ);
                categoryRepository.save(category);
            }
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());

            if (request.getCorrect_option() > request.getOptions().size()) {
                throw new InvalidValueException("Correct option is greater than the number of options!");
            }


            String questionImagePath = null;
            if(questionImage!=null && !questionImage.isEmpty()) {
                questionImagePath = saveImage(questionImage);
            }

            McqQuestions newMcqQuestion;

            newMcqQuestion  = new McqQuestions(
                    request.getQuestion(),
                    difficulty,
                    request.getCorrect_option(),
                    category,
                    questionImagePath
            );

            mcqQuestionsRepository.save(newMcqQuestion);

            List<AddMcqOptionRequest> options = request.getOptions();
            System.out.println(optionImages);


            for(int i=0;i<options.size();i++){
                String optionImage = null;

                if(optionImages != null && i < optionImages.size() && optionImages.get(i) != null && !optionImages.get(i).isEmpty()) {
                    optionImage = saveImage(optionImages.get(i));
                }

                McqOptions newOption = new McqOptions(
                  i+1,
                  options.get(i).getText(),
                  optionImage
                );

                newOption.setMcqQuestions(newMcqQuestion);
                mcqOptionsRepository.save(newOption);

            }

        } catch (IllegalArgumentException | JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new InvalidValueException("Invalid difficulty!");
        }

    }


    private String saveImage(MultipartFile file) throws IOException, InvalidValueException {

        try{

            String imageType = file.getContentType().split("/")[1];

            if(!imageType.equals("png") && !imageType.equals("jpg") && !imageType.equals("jpeg")){
                throw new InvalidValueException("Invalid file type.");
            }


            String uploadDIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();
            String imageName = System.currentTimeMillis()+"_" + file.getOriginalFilename();
            String imagePath = uploadDIR + "//" + imageName;

            Path path = Paths.get(imagePath);

            System.out.println(path);

            Files.copy(file.getInputStream(),path, REPLACE_EXISTING);

            return imageName;
        }catch (IOException e){
            throw new IOException("Failed to save image!");
        }
    }

    private void deleteImage(String imageName) throws IOException {
        try{
            String uploadDIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();

            Path imagePath = Paths.get(uploadDIR,imageName);

            if(Files.exists(imagePath)) {
                Files.delete(imagePath);
            }


        } catch (IOException e) {
            throw new IOException("Failed to delete image.");
        }
    }

    @Transactional
    @Override
    public void updateMcqQuestions(String requestJson,MultipartFile questionImage, int id) throws ResourceNotFoundException, InvalidValueException, IOException {

        try{

            ObjectMapper objectMapper  = new ObjectMapper();
            UpdateMcqQuestionRequest request = objectMapper.readValue(requestJson, UpdateMcqQuestionRequest.class);

            McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Question not found!"));

            Category category = categoryRepository.findById(request.getCategory_id()).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
            Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());


            boolean isOptionCorrect = false;
            for(McqOptions option : mcqQuestions.getMcqOptions()){
                if(option.getOption_number() == request.getCorrect_option()){
                    isOptionCorrect = true;
                    break;
                }
            }

            if(!isOptionCorrect){
                throw new InvalidValueException("Correct option is not present!");
            }


            if(questionImage!=null && !questionImage.isEmpty()){
                if(mcqQuestions.getImage()!=null && !mcqQuestions.getImage().isEmpty()){
                    deleteImage(mcqQuestions.getImage());
                }
                mcqQuestions.setImage(saveImage(questionImage));
            }

            mcqQuestions.setQuestion(request.getQuestion());
            mcqQuestions.setDifficulty(difficulty);
            mcqQuestions.setCategory(category);
            mcqQuestions.setCorrect_option(request.getCorrect_option());
            mcqQuestionsRepository.save(mcqQuestions);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw new InvalidValueException("Invalid difficulty!");
        }  catch (IOException e) {
            throw new IOException("Failed to save image!");
        }

    }

    @Transactional
    @Override
    public void deleteMcqQuestions(int id) throws ResourceNotFoundException, IOException {

        try {
            McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mcq Question not found!"));

            if(mcqQuestions.getImage()!=null && !mcqQuestions.getImage().isEmpty()){
                deleteImage(mcqQuestions.getImage());
            }

            for(McqOptions option : mcqQuestions.getMcqOptions()){

                if(option.getImage()!=null && !option.getImage().isEmpty()){
                    deleteImage(option.getImage());
                }
            }



            mcqQuestionsRepository.delete(mcqQuestions);
        } catch (IOException e) {
            throw new IOException("Failed to delete image!");
        }
    }

    @Override
    public List<AdminMcqQuestionDto> getAllMcqQuestions() {

         return getConvertedMcqQuestionsDtoList(mcqQuestionsRepository.findAll());
    }

    @Override
    public AdminMcqQuestionDto getMcqQuestionsById(int id) throws ResourceNotFoundException {
        McqQuestions mcqQuestions = mcqQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Questions not found!"));
        return convertToDto(mcqQuestions);
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


    private List<AdminMcqQuestionDto> getConvertedMcqQuestionsDtoList(List<McqQuestions> mcqQuestions){
        return mcqQuestions.stream().map(this::convertToDto).toList();
    }

    private List<McqOptionsDto> getConvertedMcqOptionsDtoList(List<McqOptionsDto> mcqOptionsDtos){
        return mcqOptionsDtos.stream().map(this::convertToDto).toList();
    }



    private AdminMcqQuestionDto convertToDto(McqQuestions mcqQuestions){
        AdminMcqQuestionDto dto = modelMapper.map(mcqQuestions,AdminMcqQuestionDto.class);
        if(dto.getImage()!=null){
            dto.setImage(getLiveImagePath(dto.getImage()));
        }
        dto.setMcqOptions(getConvertedMcqOptionsDtoList(dto.getMcqOptions()));
        return dto;
    }

    private McqOptionsDto convertToDto(McqOptionsDto mcqOptions){
        if(mcqOptions.getImage()!=null){
            mcqOptions.setImage(getLiveImagePath(mcqOptions.getImage()));
        }
        return mcqOptions;
    }


    private McqOptionsDto convertToDto(McqOptions mcqOptions){
        McqOptionsDto dto = modelMapper.map(mcqOptions,McqOptionsDto.class);
        if(mcqOptions.getImage()!=null){
            dto.setImage(getLiveImagePath(mcqOptions.getImage()));
        }
        return dto;
    }


    private String getLiveImagePath(String image){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(image).toUriString();
    }


    // ----------------------------------------------------Mcq Options---------------------------------------------

    @Transactional
    @Override
    public void addMcqOptions(String requestJson, MultipartFile optionImage) throws IOException, ResourceNotFoundException, ResourceExistsException, InvalidValueException {

        ObjectMapper objectMapper = new ObjectMapper();
        AddMcqOptionRequest request = objectMapper.readValue(requestJson,AddMcqOptionRequest.class);

        McqQuestions mcqQuestions = mcqQuestionsRepository.findById(request.getMcqQuestionId()).orElseThrow( () -> new ResourceNotFoundException("Mcq Question not found."));

        String imageName = null;
        if(optionImage!=null &&  !optionImage.isEmpty()){
            imageName = saveImage(optionImage);
        }

        for(McqOptions option : mcqQuestions.getMcqOptions()){
            if(option.getOption_number() == request.getOption_number()){
                throw new ResourceExistsException("Mcq option with same option number is already present.");
            }
        }

        McqOptions mcqOptions = new McqOptions(request.getOption_number(),request.getText(),imageName);
        mcqOptions.setMcqQuestions(mcqQuestions);
        mcqOptionsRepository.save(mcqOptions);

    }

    @Transactional
    @Override
    public void deleteMcqOptions(int id) throws ResourceNotFoundException, IOException {
        McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
        McqQuestions mcqQuestions = mcqoptions.getMcqQuestions();

        if(mcqQuestions.getCorrect_option() == mcqoptions.getOption_number()){
            throw new ResourceNotFoundException("Cannot delete correct option!");
        }

        try {
            if (mcqoptions.getImage() != null && !mcqoptions.getImage().isEmpty()) {
                deleteImage(mcqoptions.getImage());
            }
        }catch (IOException e){
            throw new IOException("Failed to delete image!");
        }

        mcqOptionsRepository.delete(mcqoptions);

    }


    @Transactional
    @Override
    public void updateMcqOptions(String requestJson,MultipartFile optionImage , int id) throws ResourceNotFoundException, InvalidValueException, ResourceExistsException {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            UpdateMcqOptionRequest request = objectMapper.readValue(requestJson,UpdateMcqOptionRequest.class);

            McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));


            if(optionImage!=null && !optionImage.isEmpty()){

                if(mcqoptions.getImage()!=null) {
                    deleteImage(mcqoptions.getImage());
                }
                mcqoptions.setImage(saveImage(optionImage));
            }

            McqQuestions mcqQuestions = mcqoptions.getMcqQuestions();

            for(McqOptions option : mcqQuestions.getMcqOptions()){
                if(option.getOption_number() == request.getOptionNumber() && id != option.getId()){
                    throw new ResourceExistsException("Mcq option with same option number is already present.");
                }
            }

            mcqoptions.setText(request.getText());
            mcqoptions.setOption_number(request.getOptionNumber());
            mcqOptionsRepository.save(mcqoptions);

        } catch (JsonProcessingException e) {
            throw new InvalidValueException("Invalid json!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
