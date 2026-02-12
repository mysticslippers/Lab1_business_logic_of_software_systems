package me.ifmo.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.ifmo.backend.entities.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private UUID id;

    @NotNull(message = "Payment.enrollmentId must not be null")
    private UUID enrollmentId;

    @Min(value = 0, message = "Payment.amountCents must be >= 0")
    private Integer amountCents;

    private PaymentStatus status;

    private String providerPaymentId;
    private String providerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
