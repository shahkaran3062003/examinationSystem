package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.InvalidENUMException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.UpdateProgrammingQuestionsRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.programmingQuestions.ProgrammingQuestionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/programming-questions")
public class ProgrammingQuestionsController {

    private final ProgrammingQuestionsService programmingQuestionsService;


    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProgrammingQuestions() {
        return ResponseEntity.ok(new ApiResponse("success", programmingQuestionsService.getAllProgrammingQuestions()));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getProgrammingQuestionById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", programmingQuestionsService.getProgrammingQuestionsById(id)));

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get/difficulty")
    public ResponseEntity<ApiResponse> getProgrammingQuestionsByDifficulty(@RequestParam(name = "difficulty") String difficulty){
        try{
            return ResponseEntity.ok(new ApiResponse("success", programmingQuestionsService.getProgrammingQuestionsByDifficulty(difficulty)));
        } catch (InvalidENUMException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProgrammingQuestion(@RequestBody @Valid AddProgrammingQuestionsRequest request) {
        try{
            programmingQuestionsService.addProgrammingQuestions(request);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question added successfully!"));
        } catch (InvalidENUMException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error",e.getMessage()));
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProgrammingQuestion(@RequestBody @Valid UpdateProgrammingQuestionsRequest request, @PathVariable int id)  {
        try{
            programmingQuestionsService.updateProgrammingQuestions(request,id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question updated successfully!"));
        }catch(InvalidENUMException | ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProgrammingQuestion(@PathVariable int id) {
        try{
            programmingQuestionsService.deleteProgrammingQuestions(id);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Question deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

}
