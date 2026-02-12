package me.ifmo.backend.controllers;

import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.PaymentDTO;
import me.ifmo.backend.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/init")
    public ResponseEntity<PaymentDTO> init(@RequestParam UUID enrollmentId,
                                           @RequestParam(required = false) String providerName) {
        PaymentDTO created = paymentService.initForEnrollment(enrollmentId, providerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentDTO> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.confirm(id));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentDTO> fail(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.fail(id));
    }

    @PostMapping("/{id}/timeout")
    public ResponseEntity<PaymentDTO> timeout(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.timeout(id));
    }

    @GetMapping("/by-enrollment/{enrollmentId}")
    public ResponseEntity<PaymentDTO> getByEnrollmentId(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(paymentService.getByEnrollmentId(enrollmentId));
    }
}
