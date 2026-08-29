package com.shah.course_management.controller;

import com.shah.course_management.dto.request.TeacherRequest;
import com.shah.course_management.dto.response.TeacherResponse;
import com.shah.course_management.service.TeacherService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;


    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(
            @RequestBody @Valid TeacherRequest teacherRequest
            ){

       TeacherResponse teacherResponse = teacherService.createTeacher(teacherRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherResponse);

    }
}
