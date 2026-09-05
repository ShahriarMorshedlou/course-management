package com.shah.course_management.repository;

import com.shah.course_management.domain.Course;
import com.shah.course_management.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    List<Teacher> findTeacherBySpecialtyStartingWithIgnoreCase(String specialty);


    @Query("""
            select  c
                        from Teacher t
                                    join t.courses c
                                                where t.id = :teacherId
            """)
    List<Course> getCoursesByTeacherId(@Param("teacherId") Long teacherId);
}
