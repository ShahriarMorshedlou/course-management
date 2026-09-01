package com.shah.course_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private Long id;
    private String title;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private TeacherSummary teacher;
}
