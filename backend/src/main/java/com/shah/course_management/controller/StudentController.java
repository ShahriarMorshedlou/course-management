package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<StudentResponse>> getAllStudent() {

        return ResponseEntity
                .ok(studentService.getAllStudent());
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


}
