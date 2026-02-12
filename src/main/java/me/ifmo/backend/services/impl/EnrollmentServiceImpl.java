package me.ifmo.backend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.EnrollmentDTO;
import me.ifmo.backend.entities.Course;
import me.ifmo.backend.entities.Enrollment;
import me.ifmo.backend.entities.EnrollmentStatus;
import me.ifmo.backend.mappers.EnrollmentMapper;
import me.ifmo.backend.repositories.CourseRepository;
import me.ifmo.backend.repositories.EnrollmentRepository;
import me.ifmo.backend.services.EnrollmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final List<EnrollmentStatus> ACTIVE_STATUSES = List.of(
            EnrollmentStatus.NEW,
            EnrollmentStatus.WAITING_PAYMENT,
            EnrollmentStatus.PAID
    );

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper mapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EnrollmentDTO enroll(String userId, Long courseId) {
        validateForEnroll(userId, courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + courseId + " not found"));

        if (Boolean.FALSE.equals(course.getActive())) {
            throw new IllegalStateException("Course with id " + course.getId() + " is not active");
        }

        boolean exists = enrollmentRepository.existsByUserIdAndCourse_IdAndStatusIn(userId, courseId, ACTIVE_STATUSES);
        if (exists) {
            throw new IllegalStateException(
                    "Active enrollment already exists for userId=" + userId + " and courseId=" + courseId
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourse(course);
        enrollment.setPriceCents(course.getPriceCents());
        enrollment.setStatus(EnrollmentStatus.NEW);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return mapper.toDto(saved);
    }

    @Override
    public EnrollmentDTO getById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment with id " + id + " not found"));
        return mapper.toDto(enrollment);
    }

    @Override
    public List<EnrollmentDTO> getByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be blank");
        }

        return enrollmentRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDto)
                .toList();
    }

    private void validateForEnroll(String userId, Long courseId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Enrollment.userId must not be blank");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("Enrollment.courseId must not be null");
        }
    }
}
