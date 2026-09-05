package com.shah.course_management.service;

import com.shah.course_management.domain.Course;
import com.shah.course_management.domain.Student;
import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.dto.response.TeacherSummary;
import com.shah.course_management.exception.StudentNotFoundException;
import com.shah.course_management.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest request) {

        Student student = new Student(
                request.getEmail(),
                request.getLastName(),
                request.getFirstName()
        );

        studentRepository.save(student);

        return new StudentResponse(
                student.getId(),
                student.getEmail(),
                student.getLastName(),
                student.getFirstName()
        );

    }

    public List<StudentResponse> getAllStudent() {

        return studentRepository.findAll()
                .stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getEmail(),
                        student.getLastName(),
                        student.getFirstName()
                )).toList();
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found With Id: " + id));

        return new StudentResponse(
                student.getId(),
                student.getEmail(),
                student.getLastName(),
                student.getFirstName()
        );
    }

    @Transactional
    public void deleteStudentById(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student Not Found With Id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, CreateStudentRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found With Id: " + id));

        student.setEmail(request.getEmail());
        student.setLastName(request.getLastName());
        student.setFirstName(request.getFirstName());

        return new StudentResponse(
                student.getId(),
                student.getEmail(),
                student.getLastName(),
                student.getFirstName()
        );
    }

    public List<StudentResponse> searchStudent(String query) {


        return studentRepository.findByFirstNameStartingWithIgnoreCase(query)
                .stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getEmail(),
                        student.getLastName(),
                        student.getFirstName()

                )).toList();

    }

    public List<CourseResponse> getCoursesByStudentId(Long studentId) {

            return studentRepository.findCourseByStudentId(studentId)
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

                    })
                    .toList();
        }
}
