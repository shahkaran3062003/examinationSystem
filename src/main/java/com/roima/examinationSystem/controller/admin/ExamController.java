package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ExamException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.request.AddDeleteExamMcqQuestionsRequest;
import com.roima.examinationSystem.request.AddDeleteExamProgrammingQuestionsRequest;
import com.roima.examinationSystem.request.AddUpdateExamRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.examManagement.ExamManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/exam")
public class ExamController {
    private final ExamManagementService examManagementService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllExams() {
        return ResponseEntity.ok(new ApiResponse("success", examManagementService.getAllExams()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getExamById(@PathVariable int id) {
        try {
            Exam exam = examManagementService.getExamById(id);
            return ResponseEntity.ok(new ApiResponse("success", exam));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/all/mcq-questions/{examId}")
    public ResponseEntity<ApiResponse> getAllMcqQuestions(@PathVariable int examId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.getAllMcqQuestionsByExam(examId)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/all/programming-questions/{examId}")
    public ResponseEntity<ApiResponse> getAllProgrammingQuestions(@PathVariable int examId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.getAllProgrammingQuestionsByExam(examId)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/college/{id}")
    public ResponseEntity<ApiResponse> getExamsByCollegeId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.getAllExamsByCollegeId(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/difficulty")
    public ResponseEntity<ApiResponse> getExamsByDifficulty(@RequestParam("difficulty") String difficulty) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.getAllExamsByDifficulty(difficulty)));
        } catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExam(@RequestBody @Valid AddUpdateExamRequest request) {
        try {
            examManagementService.addExam(request);
            return ResponseEntity.ok(new ApiResponse("success", "Exam added successfully!"));
        } catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/generate/{examId}")
    public ResponseEntity<ApiResponse> generateExam(@PathVariable int examId) {
        try{
            examManagementService.generateExam(examId);
            return ResponseEntity.ok(new ApiResponse("success", "Exam generated successfully!"));
        }catch (ResourceNotFoundException | ExamException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add/mcq-questions/{examId}")
    public ResponseEntity<ApiResponse> addMcqExam(@RequestBody @Valid AddDeleteExamMcqQuestionsRequest request, @PathVariable int examId) {
        try {
            examManagementService.addExamMcqQuestions(request,examId);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Questions added successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/add/programming-questions/{examId}")
    public ResponseEntity<ApiResponse> addProgrammingExam(@RequestBody @Valid AddDeleteExamProgrammingQuestionsRequest request, @PathVariable int examId) {
        try{
            examManagementService.addProgrammingQuestions(request,examId);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Questions added successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateExam(@RequestBody @Valid AddUpdateExamRequest request, @PathVariable int id) {
        try {
            examManagementService.updateExam(request, id);
            return ResponseEntity.ok(new ApiResponse("success", "Exam updated successfully!"));
        } catch (ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteExam(@PathVariable int id) {
        try {
            examManagementService.deleteExam(id);
            return ResponseEntity.ok(new ApiResponse("success", "Exam deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/mcq-questions/{examId}")
    public ResponseEntity<ApiResponse> deleteMcqQuestions(@RequestBody @Valid AddDeleteExamMcqQuestionsRequest request, @PathVariable int examId) {
        try {
            examManagementService.deleteExamMcqQuestions(request,examId);
            return ResponseEntity.ok(new ApiResponse("success", "Mcq Questions deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/programming-questions/{examId}")
    public ResponseEntity<ApiResponse> deleteProgrammingQuestions(@RequestBody @Valid AddDeleteExamProgrammingQuestionsRequest request, @PathVariable int examId) {
        try {
            examManagementService.deleteExamProgrammingQuestions(request,examId);
            return ResponseEntity.ok(new ApiResponse("success", "Programming Questions deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
