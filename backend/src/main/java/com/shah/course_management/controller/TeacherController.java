package com.shah.course_management.controller;

import com.shah.course_management.dto.request.TeacherRequest;
import com.shah.course_management.dto.response.TeacherResponse;
import com.shah.course_management.service.TeacherService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    ) {

        TeacherResponse teacherResponse = teacherService.createTeacher(teacherRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherResponse);

    }


    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        List<TeacherResponse> teacherResponse = teacherService.getAllTeachers();

        return ResponseEntity
                .ok(teacherResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(
            @PathVariable
            @Positive
            Long id) {

        TeacherResponse teacherResponse = teacherService.getTeacherById(id);

        return ResponseEntity
                .ok(teacherResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacherById(
            @PathVariable
            @Positive
            Long id
    ) {

        teacherService.deleteTeacherById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacherById(
            @PathVariable
            @Positive
            Long id,
            @RequestBody
            @Valid
            TeacherRequest request
    ) {
        TeacherResponse teacherResponse = teacherService.updateTeacherById(id, request);

        return ResponseEntity
                .ok(teacherResponse);
    }
}
