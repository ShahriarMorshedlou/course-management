package com.shah.course_management;


import com.shah.course_management.domain.Teacher;
import com.shah.course_management.dto.request.TeacherRequest;
import com.shah.course_management.dto.response.StudentResponse;
import com.shah.course_management.dto.response.TeacherResponse;
import com.shah.course_management.exception.TeacherNotFoundException;
import com.shah.course_management.repository.TeacherRepository;
import com.shah.course_management.service.TeacherService;
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
public class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepository;

    @InjectMocks
    TeacherService teacherService;


    Teacher teacher = new Teacher(
            "elon",
            "musk",
            "@email.com",
            "businessman"
    );

    @Test
    void createTeacher_shouldCreatAndSaveTeacher() {

        TeacherRequest request = new TeacherRequest(
                "elon",
                "musk",
                "@email.com",
                "businessman"
        );

        teacherService.createTeacher(request);

        verify(teacherRepository).save(any(Teacher.class));

    }

    @Test
    void creatTeacher_shouldReturnTeacherResponse() {

        TeacherRequest request = new TeacherRequest(
                "elon",
                "musk",
                "@email.com",
                "businessman"
        );

        TeacherResponse result = teacherService.createTeacher(request);

        assertEquals("elon", result.getFirstName());
        assertEquals("musk", result.getLastName());
        assertEquals("@email.com", result.getEmail());
        assertEquals("businessman", result.getSpecialty());

    }


    @Test
    void getTeacherById_shouldReturnTeacher() {

        when(teacherRepository.findById(5L))
                .thenReturn(Optional.of(teacher));

        TeacherResponse result = teacherService.getTeacherById(5L);

        assertEquals("elon", result.getFirstName());
        assertEquals("musk", result.getLastName());
        assertEquals("@email.com", result.getEmail());
        assertEquals("businessman", result.getSpecialty());

        verify(teacherRepository).findById(5L);
    }

    @Test
    void getTeacherById_shouldThrowNotFoundTeacherException() {

        when(teacherRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> {
            teacherService.getTeacherById(999L);
        });
    }

    @Test
    void deleteTeacher_shouldDeleteTeacher() {

        when(teacherRepository.existsById(5L))
                .thenReturn(true);

        teacherService.deleteTeacherById(5L);

        verify(teacherRepository).deleteById(5L);

    }

    @Test
    void deleteTeacher_shouldThrowNotFoundTeacherException() {

        when(teacherRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(TeacherNotFoundException.class, () -> {
            teacherService.deleteTeacherById(999L);
        });

    }

    @Test
    void updateTeacherById_shouldFindTeacherById() {

        TeacherRequest request = new TeacherRequest(
                "elon",
                "musk",
                "@email.com",
                "businessman"
        );

        when(teacherRepository.findById(5L))
                .thenReturn(Optional.of(teacher));

        teacherService.updateTeacherById(5L, request);

        verify(teacherRepository).findById(5L);
    }


    @Test
    void updateTeacherById_shouldReturnUpdateTeacher() {

        TeacherRequest request = new TeacherRequest(
                "elon",
                "musk",
                "@email.com",
                "businessman"
        );


        when(teacherRepository.findById(5L))
                .thenReturn(Optional.of(teacher));

        TeacherResponse result =
                teacherService.updateTeacherById(5L, request);


        assertEquals("elon", result.getFirstName());
        assertEquals("musk", result.getLastName());
        assertEquals("@email.com", result.getEmail());
        assertEquals("businessman", result.getSpecialty());

    }

    @Test
    void searchTeacher_shouldFindTeachersBySpecialty() {

        String specialty = "business";

        when(teacherRepository.findTeacherBySpecialtyStartingWithIgnoreCase(specialty))
                .thenReturn(List.of(teacher));

        teacherService.searchTeacher(specialty);

        verify(teacherRepository)
                .findTeacherBySpecialtyStartingWithIgnoreCase(specialty);
    }


    @Test
    void searchTeacher_shouldReturnTeacherResponses() {

        String specialty = "business";

        when(teacherRepository.findTeacherBySpecialtyStartingWithIgnoreCase(specialty))
                .thenReturn(List.of(teacher));

        List<TeacherResponse> result =
                teacherService.searchTeacher(specialty);

        assertEquals("elon", result.get(0).getFirstName());
        assertEquals("musk", result.get(0).getLastName());
        assertEquals("@email.com", result.get(0).getEmail());
        assertEquals("businessman", result.get(0).getSpecialty());
    }

}
