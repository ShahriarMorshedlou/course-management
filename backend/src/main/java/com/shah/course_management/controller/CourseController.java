package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    public CourseResponse createCourseResponse(@Valid @RequestBody CreateCourseRequest request) {
        return courseService.createCourse(request);
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(
            @PathVariable
            @Positive
            Long id) {
        return courseService.getCourseById(id);

    }



}
