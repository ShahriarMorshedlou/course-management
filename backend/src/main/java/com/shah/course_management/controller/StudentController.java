package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/student")
@Validated
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public ResponseEntity<StudentResponse> creatStudent(
            @RequestBody
            @Valid
            CreateStudentRequest request
    ) {
        StudentResponse studentResponse = studentService.createStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentResponse);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudent(Pageable pageable) {

        return ResponseEntity
                .ok(studentService.getAllStudent(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable
            @Positive
            Long id
    ) {
        return ResponseEntity
                .ok(studentService.getStudentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(
            @PathVariable
            @Positive
            Long id
    ) {

        studentService.deleteStudentById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable
            @Positive
            Long id,
            @RequestBody
            @Valid
            CreateStudentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.updateStudent(id, request));
    }


    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudent(
            @RequestParam
            String query
    ) {
        return ResponseEntity
                .ok(studentService.searchStudent(query));
    }

}
