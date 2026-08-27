package com.shah.course_management.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseResponse {

    private Long id;
    private String title;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;

    public CourseResponse(Long id, String title, String code, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
