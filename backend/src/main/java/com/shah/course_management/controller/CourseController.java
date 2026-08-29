package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.request.UpdateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/course")
@Validated
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public ResponseEntity<CourseResponse> createCourseResponse(@Valid @RequestBody CreateCourseRequest request) {
        CourseResponse courseResponse = courseService.createCourse(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseResponse);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courseResponses = courseService.getAllCourses();

        return ResponseEntity
                .ok(courseResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable
            @Positive
            Long id) {
        CourseResponse courseResponse = courseService.getCourseById(id);

        return ResponseEntity
                .ok(courseResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable @Positive Long id) {
        courseService.deleteCourseById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UpdateCourseRequest updateCourseRequest) {
        CourseResponse courseResponse = courseService.updateCourse(id, updateCourseRequest);

        return ResponseEntity
                .ok(courseResponse);
    }

}
