package com.shah.course_management.service;

import com.shah.course_management.domain.Teacher;
import com.shah.course_management.dto.request.TeacherRequest;
import com.shah.course_management.dto.response.TeacherResponse;
import com.shah.course_management.exception.TeacherNotFoundException;
import com.shah.course_management.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest teacherRequest) {

        Teacher teacher = new Teacher(
                teacherRequest.getFirstName(),
                teacherRequest.getLastName(),
                teacherRequest.getEmail(),
                teacherRequest.getSpecialty()

        );

        teacherRepository.save(teacher);

        return new TeacherResponse(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getSpecialty()
        );
    }

    public Page<TeacherResponse> getAllTeachers(Pageable pageable) {

        return teacherRepository.findAll(pageable)
                .map(teacher -> new TeacherResponse(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getEmail(),
                        teacher.getSpecialty()
                ));
    }

    public TeacherResponse getTeacherById(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher Not Found With Id: " + id));

        return new TeacherResponse(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getSpecialty()

        );
    }

    @Transactional
    public void deleteTeacherById(Long id) {

        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException("Teacher Not Found With Id: " + id);
        }
        teacherRepository.deleteById(id);
    }

    @Transactional
    public TeacherResponse updateTeacherById(Long id, TeacherRequest request) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher Not Found With Id: " + id));

        teacher.setEmail(request.getEmail());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setSpecialty(request.getSpecialty());

        return new TeacherResponse(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getSpecialty()
        );
    }

    public List<TeacherResponse> searchTeacher(String specialty) {

        return teacherRepository.findTeacherBySpecialtyStartingWithIgnoreCase(specialty)
                .stream()
                .map(teacher -> new TeacherResponse(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getEmail(),
                        teacher.getSpecialty()
                )).toList();

    }
}
