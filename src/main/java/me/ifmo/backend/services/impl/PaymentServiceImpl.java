package me.ifmo.backend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.PaymentDTO;
import me.ifmo.backend.entities.Enrollment;
import me.ifmo.backend.entities.EnrollmentStatus;
import me.ifmo.backend.entities.Payment;
import me.ifmo.backend.entities.PaymentStatus;
import me.ifmo.backend.mappers.PaymentMapper;
import me.ifmo.backend.repositories.EnrollmentRepository;
import me.ifmo.backend.repositories.PaymentRepository;
import me.ifmo.backend.services.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentMapper mapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PaymentDTO initForEnrollment(UUID enrollmentId, String providerName) {
        if (enrollmentId == null) {
            throw new IllegalArgumentException("enrollmentId must not be null");
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment with id " + enrollmentId + " not found"));

        if (enrollment.getStatus() == EnrollmentStatus.CANCELED || enrollment.getStatus() == EnrollmentStatus.EXPIRED) {
            throw new IllegalStateException("Cannot init payment for enrollment in status " + enrollment.getStatus());
        }

        if (enrollment.getStatus() == EnrollmentStatus.PAID) {
            throw new IllegalStateException("Enrollment already PAID: " + enrollmentId);
        }

        Payment payment = paymentRepository.findByEnrollment_Id(enrollmentId).orElse(null);
        if (payment != null) {
            // если payment уже есть — просто возвращаем, но enrollment переводим в WAITING_PAYMENT (если вдруг NEW)
            if (enrollment.getStatus() == EnrollmentStatus.NEW) {
                enrollment.setStatus(EnrollmentStatus.WAITING_PAYMENT);
                enrollmentRepository.save(enrollment);
            }
            return mapper.toDto(payment);
        }

        enrollment.setStatus(EnrollmentStatus.WAITING_PAYMENT);
        enrollmentRepository.save(enrollment);

        Payment created = new Payment();
        created.setEnrollment(enrollment);
        created.setAmountCents(enrollment.getPriceCents());
        created.setStatus(PaymentStatus.INIT);
        created.setProviderName(normalizeProvider(providerName));
        created.setProviderPaymentId("prov_" + UUID.randomUUID());

        Payment saved = paymentRepository.save(created);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PaymentDTO confirm(UUID paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);

        Enrollment enrollment = payment.getEnrollment();
        if (enrollment.getStatus() == EnrollmentStatus.CANCELED || enrollment.getStatus() == EnrollmentStatus.EXPIRED) {
            throw new IllegalStateException("Cannot confirm payment for enrollment in status " + enrollment.getStatus());
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        enrollment.setStatus(EnrollmentStatus.PAID);

        paymentRepository.save(payment);
        enrollmentRepository.save(enrollment);

        return mapper.toDto(payment);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PaymentDTO fail(UUID paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);

        Enrollment enrollment = payment.getEnrollment();
        if (enrollment.getStatus() == EnrollmentStatus.PAID) {
            throw new IllegalStateException("Cannot fail payment for already PAID enrollment: " + enrollment.getId());
        }

        payment.setStatus(PaymentStatus.FAILED);
        enrollment.setStatus(EnrollmentStatus.CANCELED);

        paymentRepository.save(payment);
        enrollmentRepository.save(enrollment);

        return mapper.toDto(payment);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PaymentDTO timeout(UUID paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);

        Enrollment enrollment = payment.getEnrollment();
        if (enrollment.getStatus() == EnrollmentStatus.PAID) {
            throw new IllegalStateException("Cannot timeout payment for already PAID enrollment: " + enrollment.getId());
        }

        payment.setStatus(PaymentStatus.TIMEOUT);
        enrollment.setStatus(EnrollmentStatus.EXPIRED);

        paymentRepository.save(payment);
        enrollmentRepository.save(enrollment);

        return mapper.toDto(payment);
    }

    @Override
    public PaymentDTO getByEnrollmentId(UUID enrollmentId) {
        Payment payment = paymentRepository.findByEnrollment_Id(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment for enrollmentId=" + enrollmentId + " not found"));
        return mapper.toDto(payment);
    }

    private Payment getPaymentOrThrow(UUID paymentId) {
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId must not be null");
        }
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id " + paymentId + " not found"));
    }

    private String normalizeProvider(String providerName) {
        if (providerName == null || providerName.isBlank()) {
            return "dummy";
        }
        String trimmed = providerName.trim();
        return trimmed.length() > 100 ? trimmed.substring(0, 100) : trimmed;
    }
}
