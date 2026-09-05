package com.shah.course_management.service;

import com.shah.course_management.domain.Course;
import com.shah.course_management.domain.Student;
import com.shah.course_management.domain.Teacher;
import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.request.UpdateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.dto.response.TeacherResponse;
import com.shah.course_management.dto.response.TeacherSummary;
import com.shah.course_management.exception.CourseNotFoundException;
import com.shah.course_management.exception.StudentNotFoundException;
import com.shah.course_management.exception.TeacherNotFoundException;
import com.shah.course_management.repository.CourseRepository;
import com.shah.course_management.repository.StudentRepository;
import com.shah.course_management.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository,TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
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

        TeacherSummary teacherSummary = null;
        if (course.getTeacher() != null) {
            teacherSummary = new TeacherSummary(
                    course.getTeacher().getFirstName(),
                    course.getTeacher().getLastName()
            );
        }

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate(),
                teacherSummary
        );
    }

    public List<CourseResponse> getAllCourses() {

        List<Course> courses = courseRepository.findAll();

        List<CourseResponse> courseResponses = courses.stream()
                .map(course -> {

                    TeacherSummary teacherSummary = null;
                    if (course.getTeacher() != null) {
                        teacherSummary = new TeacherSummary(
                                course.getTeacher().getFirstName(),
                                course.getTeacher().getLastName()
                        );
                    }

                    return new CourseResponse(
                            course.getId(),
                            course.getTitle(),
                            course.getCode(),
                            course.getStartDate(),
                            course.getEndDate(),
                            teacherSummary
                    );
                }).toList();


        return courseResponses;
    }

    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

        TeacherSummary teacherSummary = null;
        if (course.getTeacher() != null) {
            teacherSummary = new TeacherSummary(
                    course.getTeacher().getFirstName(),
                    course.getTeacher().getLastName()
            );
        }

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate(),
                teacherSummary
        );
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


        TeacherSummary teacherSummary = null;
        if (course.getTeacher() != null) {
            teacherSummary = new TeacherSummary(
                    course.getTeacher().getFirstName(),
                    course.getTeacher().getLastName()
            );
        }

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStartDate(),
                course.getEndDate(),
                teacherSummary
        );
    }

    public List<CourseResponse> searchCourse(String title) {

        return courseRepository.findCourseByTitleStartingWithIgnoreCase(title)
                .stream()
                .map(course -> {

                    TeacherSummary teacherSummary = null;
                    if (course.getTeacher() != null) {
                        teacherSummary = new TeacherSummary(
                                course.getTeacher().getFirstName(),
                                course.getTeacher().getLastName()
                        );
                    }

                    return new CourseResponse(
                            course.getId(),
                            course.getTitle(),
                            course.getCode(),
                            course.getStartDate(),
                            course.getEndDate(),
                            teacherSummary
                    );
                }).toList();

    }

    public List<StudentResponse> getStudentsByCourseId(Long courseId) {


        List<Student> students =
                courseRepository.findStudentsByCourseId(courseId);

        return students.stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getEmail(),
                        student.getLastName(),
                        student.getFirstName()
                ))
                .toList();

    }


    @Transactional
    public void enrollStudentInCourse(Long courseId, Long studentId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("course not found with id: " + courseId));


        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("student not found with id: " + studentId));


        course.getStudents().add(student);

    }

    @Transactional
    public void enrollTeacherInCourse(Long courseId, Long teacherId){

        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("course not found exception with id: " + courseId));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new TeacherNotFoundException("teacher not found with id: " + teacherId));


        course.setTeacher(teacher);


    }
}
