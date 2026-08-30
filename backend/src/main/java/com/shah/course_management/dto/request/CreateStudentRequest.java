package com.shah.course_management.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 2 , max = 15)
    private String lastName;

    @NotBlank
    @Size (min = 2 , max = 15)
    private String firstName;
}
