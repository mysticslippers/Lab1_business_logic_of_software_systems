package me.ifmo.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Long id;

    @NotBlank(message = "Course.title must not be blank")
    private String title;

    private String description;

    @NotNull(message = "Course.priceCents must not be null")
    @Min(value = 0, message = "Course.priceCents must be >= 0")
    private Integer priceCents;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
