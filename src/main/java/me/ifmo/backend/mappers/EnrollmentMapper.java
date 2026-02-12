package me.ifmo.backend.mappers;

import me.ifmo.backend.DTO.EnrollmentDTO;
import me.ifmo.backend.entities.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "course.id", target = "courseId")
    EnrollmentDTO toDto(Enrollment enrollment);
}
