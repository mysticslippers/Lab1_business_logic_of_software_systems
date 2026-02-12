package me.ifmo.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.EnrollmentDTO;
import me.ifmo.backend.services.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentDTO> enroll(@Valid @RequestBody EnrollmentDTO dto) {
        EnrollmentDTO created = enrollmentService.enroll(dto.getUserId(), dto.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(enrollmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(enrollmentService.getByUserId(userId));
    }
}
