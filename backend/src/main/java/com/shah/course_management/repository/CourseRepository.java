package com.shah.course_management.repository;

import com.shah.course_management.domain.Course;
import com.shah.course_management.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCourseByTitleStartingWithIgnoreCase(String title);

    @Query("""
    SELECT s
    FROM Course c
    JOIN c.students s
    WHERE c.id = :courseId
""")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);
}
