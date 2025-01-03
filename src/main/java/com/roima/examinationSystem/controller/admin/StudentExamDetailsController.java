package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.studentManagement.StudentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/student-exam-details")
public class StudentExamDetailsController {

    private final StudentManagementService studentManagementService;

    @GetMapping("/get/all/student/{id}")
    public ResponseEntity<ApiResponse> getAllStudentExamDetailsByStudentId(@PathVariable int id) {
        return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getAllStudentExamDetailsByStudentId(id)));
    }

    @GetMapping("/get/student/{studentId}/exam/{examId}")
    public ResponseEntity<ApiResponse> getStudentExamDetailsByStudentIdAndExamId(@PathVariable int studentId, @PathVariable int examId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getStudentExamDetailsByStudentIdAndExamId(studentId, examId)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/exam/{id}/result")
    public ResponseEntity<ApiResponse> getAllStudentExamDetailsByExamId(@PathVariable int id) {
        return ResponseEntity.ok(new ApiResponse("success", studentManagementService.getAllStudentExamDetailsByExamId(id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStudentExamDetails(@PathVariable int id) {
        try {
            studentManagementService.deleteStudentExamDetailsById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Student Exam Details deleted successfully!"));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
