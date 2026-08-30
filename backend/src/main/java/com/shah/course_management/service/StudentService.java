package com.shah.course_management.service;

import com.shah.course_management.domain.Student;
import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest request){

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
}
