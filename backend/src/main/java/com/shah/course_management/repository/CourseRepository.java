package com.shah.course_management.repository;

import com.shah.course_management.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCourseByTitleStartingWithIgnoreCase(String title);
}
