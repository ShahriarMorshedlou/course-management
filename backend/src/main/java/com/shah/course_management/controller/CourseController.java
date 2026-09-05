package com.shah.course_management.controller;

import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.request.UpdateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
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

    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> searchCourse(
            @RequestParam
            String title
    ){
        return ResponseEntity
                .ok(courseService.searchCourse(title));
    }


    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourseId (
            @PathVariable
            Long courseId
    ){
        return ResponseEntity.ok(
                courseService.getStudentsByCourseId(courseId)
        );
    }

    @PostMapping("/{courseId}/{studentId}")
    public ResponseEntity<Void> enrollStudentInCourse(
            @PathVariable
            Long courseId,
            @PathVariable
            Long studentId
    ){
        courseService.enrollStudentInCourse(courseId,studentId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity <Void> enrollTeacherInCourse(
            @PathVariable
            Long courseId,
            @PathVariable
            Long teacherId
    ){

        courseService.enrollTeacherInCourse(courseId,teacherId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
