package com.shah.course_management.service;

import com.shah.course_management.domain.Course;
import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.request.UpdateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.exception.CourseNotFoundException;
import com.shah.course_management.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
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
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        CourseResponse courseResponse = new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate()
        );
        return courseResponse;
    }

    @Transactional
    public void deleteCourseById(Long id) {

        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }

        courseRepository.deleteById(id);
    }


    @Transactional
    public CourseResponse updateCourse(Long id, UpdateCourseRequest updateCourseRequest) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException
                                ("Course Not Found With Id: " + id));


        course.setTitle(updateCourseRequest.getTitle());
        course.setStartDate(updateCourseRequest.getStartDate());
        course.setEndDate(updateCourseRequest.getEndDate());


        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate()
        );
    }

    public List<CourseResponse> searchCourse(String title) {

        return courseRepository.findCourseByTitleStartingWithIgnoreCase(title)
                .stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getCode(),
                        course.getStartDate(),
                        course.getEndDate()
                )).toList();


    }


}
