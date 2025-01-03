package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.studentManagement.StudentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/student-mcq-answer")
public class StudentMcqAnswerController {

    private final StudentManagementService studentManagementService;


    @GetMapping("get/student-exam-details/{studentExamDetailsId}")
    public ResponseEntity<ApiResponse> getAllMcqAnswerByStudentId(@PathVariable int studentExamDetailsId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentMcqAnswerByStudentId(studentExamDetailsId)));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse> getMcqAnswerById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentMcqAnswerById(id)));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }


//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse> addMcqAnswer(@RequestBody AddStudentMcqAnswerRequest request) {
//        try {
//            studentManagementService.addStudentMcqAnswer(request);
//            return ResponseEntity.ok(new ApiResponse("success","Mcq Answer added successfully" ));
//        }catch (ResourceNotFoundException e){
//            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMcqAnswer(@PathVariable int id) {
        try {
            studentManagementService.deleteStudentMcqAnswerById(id);
            return ResponseEntity.ok(new ApiResponse("success","Mcq Answer deleted successfully" ));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
