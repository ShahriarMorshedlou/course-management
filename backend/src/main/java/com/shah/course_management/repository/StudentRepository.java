package com.shah.course_management.repository;

import com.shah.course_management.domain.Course;
import com.shah.course_management.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByFirstNameStartingWithIgnoreCase(String query);


    @Query("""
            select c
            FROM Student s
                        join s.courses c
                                    where s.id = :studentId 
            
            """)
    List<Course> findCourseByStudentId(@Param("studentId") Long studentId);
}