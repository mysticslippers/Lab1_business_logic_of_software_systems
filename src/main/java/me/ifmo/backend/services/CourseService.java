package me.ifmo.backend.services;

import me.ifmo.backend.DTO.CourseDTO;

import java.util.List;

public interface CourseService {

    List<CourseDTO> getActiveCourses();

    CourseDTO getById(Long id);
}
