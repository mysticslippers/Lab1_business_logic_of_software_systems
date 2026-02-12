package me.ifmo.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "UserId must not be blank")
    @Size(max = 64, message = "UserId length must be <= 64")
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @NotNull(message = "Course must not be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be >= 0")
    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.status == null) {
            this.status = EnrollmentStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", courseId=" + (course != null ? course.getId() : null) +
                ", priceCents=" + priceCents +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment enrollment = (Enrollment) o;
        return id != null && id.equals(enrollment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
