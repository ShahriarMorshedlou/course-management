package com.shah.course_management;

import com.shah.course_management.domain.Course;
import com.shah.course_management.dto.request.CreateCourseRequest;
import com.shah.course_management.dto.request.UpdateCourseRequest;
import com.shah.course_management.dto.response.CourseResponse;
import com.shah.course_management.repository.CourseRepository;
import com.shah.course_management.service.CourseService;
import com.shah.course_management.exception.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseService courseService;

    Course course = new Course(
            "JAVA101",
            "Java Programming",
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2026, 12, 1)
    );


    @Test
    void createCourse_shouldCreateAndSaveCourse() {

        CreateCourseRequest request = new CreateCourseRequest();

        request.setTitle("Java Programming");
        request.setCode("JAVA1");
        request.setStartDate(LocalDate.of(2026, 9, 1));
        request.setEndDate(LocalDate.of(2026, 12, 1));

        courseService.createCourse(request);

        verify(courseRepository).save(any(Course.class));
    }


    @Test
    void createCourse_shouldReturnCourseResponse() {

        CreateCourseRequest request = new CreateCourseRequest();

        request.setTitle("Java Programming");
        request.setCode("JAVA1");
        request.setStartDate(LocalDate.of(2026, 9, 1));
        request.setEndDate(LocalDate.of(2026, 12, 1));

        CourseResponse result =
                courseService.createCourse(request);

        assertEquals("Java Programming", result.getTitle());
        assertEquals("JAVA1", result.getCode());
        assertEquals(LocalDate.of(2026, 9, 1), result.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 1), result.getEndDate());
    }

    @Test
    void getAllCourses_shouldReturnCourses() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> coursePage =
                new PageImpl<>(List.of(course), pageable, 1);

        when(courseRepository.findAll(pageable))
                .thenReturn(coursePage);

        Page<CourseResponse> result =
                courseService.getAllCourses(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Java Programming", result.getContent().get(0).getTitle());
        assertEquals("JAVA101", result.getContent().get(0).getCode());
    }

    @Test
    void getCourseById_shouldReturnCourse() {

        when(courseRepository.findById(5L))
                .thenReturn(Optional.of(course));

        CourseResponse result =
                courseService.getCourseById(5L);

        assertEquals("Java Programming", result.getTitle());
        assertEquals("JAVA101", result.getCode());
        assertEquals(LocalDate.of(2026, 9, 1), result.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 1), result.getEndDate());

        verify(courseRepository).findById(5L);
    }


    @Test
    void getCourseById_shouldThrowNotFoundCourseException() {

        when(courseRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourseById(999L);
        });
    }


    @Test
    void deleteCourse_shouldDeleteCourse() {

        when(courseRepository.existsById(5L))
                .thenReturn(true);

        courseService.deleteCourseById(5L);

        verify(courseRepository).deleteById(5L);
    }


    @Test
    void deleteCourse_shouldThrowNotFoundCourseException() {

        when(courseRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourseById(999L);
        });
    }


    @Test
    void updateCourse_shouldFindCourseById() {

        UpdateCourseRequest request = new UpdateCourseRequest(
                "Advanced Java",
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 12, 10)
        );

        when(courseRepository.findById(5L))
                .thenReturn(Optional.of(course));

        courseService.updateCourse(5L, request);

        verify(courseRepository).findById(5L);
    }


    @Test
    void updateCourse_shouldReturnUpdatedCourseResponse() {

        UpdateCourseRequest request = new UpdateCourseRequest(
                "Advanced Java",
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 12, 10)
        );

        when(courseRepository.findById(5L))
                .thenReturn(Optional.of(course));

        CourseResponse result =
                courseService.updateCourse(5L, request);

        assertEquals("Advanced Java", result.getTitle());
        assertEquals("JAVA101", result.getCode());
        assertEquals(LocalDate.of(2026, 9, 10), result.getStartDate());
        assertEquals(LocalDate.of(2026, 12, 10), result.getEndDate());
    }


    @Test
    void searchCourse_shouldFindCoursesByTitle() {

        String title = "Java";

        when(courseRepository.findCourseByTitleStartingWithIgnoreCase(title))
                .thenReturn(List.of(course));

        courseService.searchCourse(title);

        verify(courseRepository)
                .findCourseByTitleStartingWithIgnoreCase(title);
    }


    @Test
    void searchCourse_shouldReturnCourseResponses() {

        String title = "Java";

        when(courseRepository.findCourseByTitleStartingWithIgnoreCase(title))
                .thenReturn(List.of(course));

        List<CourseResponse> result =
                courseService.searchCourse(title);

        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
        assertEquals("JAVA101", result.get(0).getCode());
        assertEquals(LocalDate.of(2026, 9, 1), result.get(0).getStartDate());
        assertEquals(LocalDate.of(2026, 12, 1), result.get(0).getEndDate());
    }
}