package me.ifmo.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.ifmo.backend.entities.EnrollmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {

    private UUID id;

    @NotBlank(message = "Enrollment.userId must not be blank")
    @Size(max = 64, message = "Enrollment.userId length must be <= 64")
    private String userId;

    @NotNull(message = "Enrollment.courseId must not be null")
    private Long courseId;

    private Integer priceCents;

    private EnrollmentStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
