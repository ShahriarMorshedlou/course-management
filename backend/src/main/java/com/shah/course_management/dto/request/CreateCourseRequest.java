package com.shah.course_management.dto.request;

import com.shah.course_management.validation.ValidCourseDates;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@ValidCourseDates
public class CreateCourseRequest {

    @NotBlank
    @Size(max = 15)
    private String title;

    @NotBlank
    @Size(max = 5)
    private String code;

    @NotNull
    @Future
    private LocalDate startDate;

    @Future
    @NotNull
    private LocalDate endDate;


}
