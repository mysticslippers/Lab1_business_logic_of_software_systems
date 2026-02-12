package me.ifmo.backend.services;

import me.ifmo.backend.DTO.EnrollmentDTO;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    EnrollmentDTO enroll(String userId, Long courseId);

    EnrollmentDTO getById(UUID id);

    List<EnrollmentDTO> getByUserId(String userId);
}
