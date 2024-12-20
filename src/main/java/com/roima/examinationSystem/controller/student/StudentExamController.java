package com.roima.examinationSystem.controller.student;


import com.roima.examinationSystem.exception.ExamException;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.*;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.student.StudentExamManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.studentPrefix}/exam")
@RequiredArgsConstructor
public class StudentExamController {


    private final StudentExamManagementService examManagementService;

    @PostMapping("/get/{collegeId}")
    public ResponseEntity<ApiResponse> getExamByUserId(@PathVariable int collegeId) {
        return ResponseEntity.ok(new ApiResponse("success", examManagementService.getExamByCollegeId(collegeId)));
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse> startExam(@RequestBody @Valid StartExamRequest request) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.startExam(request)));
        }catch (ResourceNotFoundException | ResourceExistsException | ExamException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitExam(@RequestBody @Valid SubmitExamRequest request) {
        try{
            examManagementService.submitExam(request);
            return ResponseEntity.ok(new ApiResponse("success","You have successfully submitted the exam." ));

        }catch (ResourceNotFoundException | ResourceExistsException | ExamException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/submit/mcqQuestion")
    public ResponseEntity<ApiResponse> submitMcqQuestion(@RequestBody @Valid AddStudentMcqAnswerRequest request) {
        try{
            examManagementService.submitMcqQuestion(request);
            return ResponseEntity.ok(new ApiResponse("success","You have successfully submitted the mcq question." ));
        } catch (ResourceNotFoundException | ResourceExistsException | ExamException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/submit/programmingQuestion")
    public ResponseEntity<ApiResponse> submitProgrammingQuestion(@RequestBody @Valid AddStudentProgrammingAnswerRequest request) {
        try{
            examManagementService.submitProgrammingQuestion(request);
            return ResponseEntity.ok(new ApiResponse("success","You have successfully submitted the programming question." ));
        } catch (ResourceNotFoundException | ResourceExistsException | ExamException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

}
