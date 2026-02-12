package me.ifmo.backend.repositories;

import me.ifmo.backend.entities.Enrollment;
import me.ifmo.backend.entities.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    boolean existsByUserIdAndCourse_IdAndStatusIn(String userId, Long courseId, List<EnrollmentStatus> statuses);
    List<Enrollment> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
