package com.shah.course_management;

import com.shah.course_management.domain.Student;
import com.shah.course_management.dto.request.CreateStudentRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.exception.StudentNotFoundException;
import com.shah.course_management.repository.StudentRepository;
import com.shah.course_management.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentService studentService;

    Student student = new Student(
            "@email.com",
            "morshedlou",
            "shahriar"
    );

    @Test
    void getStudentById_shouldReturnStudent() {

        when(studentRepository.findById(5L))
                .thenReturn(Optional.of(student));

        StudentResponse result = studentService.getStudentById(5L);

        assertEquals("@email.com", result.getEmail());
        assertEquals("morshedlou", result.getLastName());
        assertEquals("shahriar", result.getFirstName());

        verify(studentRepository).findById(5L);

    }

    @Test
    void getStudentById_shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findById(9999L))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(9999L);
        });
    }

    @Test
    void createStudent_shouldCreateAndSaveStudent() {

        CreateStudentRequest request =
                new CreateStudentRequest(

                        "@email.com",
                        "morshedlou",
                        "shahriar"
                );

        studentService.createStudent(request);

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_shouldReturnStudentResponse() {

        CreateStudentRequest request = new CreateStudentRequest(
                "@email.com",
                "morshedlou",
                "shahriar"
        );

        StudentResponse result = studentService.createStudent(request);

        assertEquals("@email.com", result.getEmail());
        assertEquals("morshedlou", result.getLastName());
        assertEquals("shahriar", result.getFirstName());
    }


    @Test
    void deleteStudent_shouldReturnStudent() {

        when(studentRepository.existsById(5L))
                .thenReturn(true);

        studentService.deleteStudentById(5L);

        verify(studentRepository).deleteById(5L);

    }

    @Test
    void deleteStudent_shouldDeleteStudentWithId() {

        when(studentRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudentById(999L);
        });

    }


    @Test
    void updateStudent_shouldFindStudentById() {

        CreateStudentRequest request = new CreateStudentRequest(
                "@email.com",
                "morshedlou",
                "shahriar"

        );

        when(studentRepository.findById(5L))
                .thenReturn(Optional.of(student));

        studentService.updateStudent(5L, request);

        verify(studentRepository).findById(5L);
    }

    @Test
    void updateStudent_shouldReturnUpdateStudent() {

        CreateStudentRequest request = new CreateStudentRequest(
                "@email.com",
                "morshedlou",
                "shahriar"

        );

        when(studentRepository.findById(5L))
                .thenReturn(Optional.of(student));

        StudentResponse result =
                studentService.updateStudent(5L, request);


        assertEquals("@email.com", result.getEmail());
        assertEquals("morshedlou", result.getLastName());
        assertEquals("shahriar", result.getFirstName());


    }

    @Test
    void searchStudent_shouldFindStudentsByFirstName(){
        String query = "sh";

        when(studentRepository.findByFirstNameStartingWithIgnoreCase(query))
                .thenReturn(List.of(student));

        studentService.searchStudent(query);

        verify(studentRepository).findByFirstNameStartingWithIgnoreCase(query);
    }

    @Test
    void searchStudent_shouldReturnStudentResponses() {

        String query = "sh";

        when(studentRepository.findByFirstNameStartingWithIgnoreCase(query))
                .thenReturn(List.of(student));

        List<StudentResponse> result =
                studentService.searchStudent(query);

        assertEquals(1, result.size());
        assertEquals("@email.com", result.get(0).getEmail());
        assertEquals("morshedlou", result.get(0).getLastName());
        assertEquals("shahriar", result.get(0).getFirstName());
    }

}
