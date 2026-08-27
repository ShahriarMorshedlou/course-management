package com.shah.course_management.service;

import com.shah.course_management.domain.Course;
import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.repository.CourseRepository;
import org.springframework.stereotype.Service;

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
}
