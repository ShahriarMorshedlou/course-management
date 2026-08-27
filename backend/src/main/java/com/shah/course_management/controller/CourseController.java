package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public CourseResponse createCourseResponse(@Valid @RequestBody CreateCourseRequest request) {
        return courseService.createCourse(request);
    }

}
