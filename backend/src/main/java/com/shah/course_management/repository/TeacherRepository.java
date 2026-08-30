package com.shah.course_management.repository;

import com.shah.course_management.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    List<Teacher> findTeacherBySpecialtyStartingWithIgnoreCase(String specialty);
}
