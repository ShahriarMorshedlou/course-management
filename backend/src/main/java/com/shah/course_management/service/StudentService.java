package com.shah.course_management.service;

import com.shah.course_management.domain.Student;
import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.exception.StudentNotFoundException;
import com.shah.course_management.repository.StudentRepository;
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
                student.getEmail(),
                student.getLastName(),
                student.getFirstName()
        );

    }

    public List<StudentResponse> getAllStudent() {

        return studentRepository.findAll()
                .stream()
                .map(student -> new StudentResponse(
                        student.getEmail(),
                        student.getLastName(),
                        student.getFirstName()
                )).toList();
    }

    public StudentResponse getStudentById (Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new StudentNotFoundException("Student Not Found With Id: " + id));

        return new StudentResponse(
                student.getEmail(),
                student.getLastName(),
                student.getFirstName()
        );
    }
}
