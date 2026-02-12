package me.ifmo.backend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.CourseDTO;
import me.ifmo.backend.entities.Course;
import me.ifmo.backend.mappers.CourseMapper;
import me.ifmo.backend.repositories.CourseRepository;
import me.ifmo.backend.services.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper mapper;

    @Override
    public List<CourseDTO> getActiveCourses() {
        return courseRepository.findActiveCoursesOrdered().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CourseDTO getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + id + " not found"));
        return mapper.toDto(course);
    }
}
