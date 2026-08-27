package com.shah.course_management.service;

import com.shah.course_management.domain.Course;
import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.exception.CourseNotFoundException;
import com.shah.course_management.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CreateCourseRequest request) {

        Course course = new Course(
                request.getCode(),
                request.getTitle(),
                request.getStartDate(),
                request.getEndDate()
        );


        courseRepository.save(course);

        CourseResponse courseResponse = new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate()
        );
        return courseResponse;
    }

    public List<CourseResponse> getAllCourses() {

        List<Course> courses = courseRepository.findAll();

        List<CourseResponse> courseResponses = courses.stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getCode(),
                        course.getStartDate(),
                        course.getEndDate()

                )).toList();

        return courseResponses;
    }

    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow( () -> new CourseNotFoundException("Course not found with id: " + id));

        CourseResponse courseResponse = new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate()
        ); return courseResponse;
    }

}
