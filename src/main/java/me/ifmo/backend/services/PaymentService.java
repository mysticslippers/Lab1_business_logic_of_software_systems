package me.ifmo.backend.services;

import me.ifmo.backend.DTO.PaymentDTO;

import java.util.UUID;

public interface PaymentService {

    PaymentDTO initForEnrollment(UUID enrollmentId, String providerName);

    PaymentDTO confirm(UUID paymentId);

    PaymentDTO fail(UUID paymentId);

    PaymentDTO timeout(UUID paymentId);

    PaymentDTO getByEnrollmentId(UUID enrollmentId);
}
