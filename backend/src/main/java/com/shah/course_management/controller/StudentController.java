package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
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
}
