package com.shah.course_management.validation;

import com.shah.course_management.dto.request.CreateCourseRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CourseDatesValidator
        implements ConstraintValidator<ValidCourseDates, CreateCourseRequest> {
    @Override
    public boolean isValid(CreateCourseRequest request, ConstraintValidatorContext context) {
        return request.getEndDate().isAfter(request.getStartDate());
    }
}
