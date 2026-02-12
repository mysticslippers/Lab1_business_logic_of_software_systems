package me.ifmo.backend.controllers;

import lombok.RequiredArgsConstructor;
import me.ifmo.backend.DTO.CourseDTO;
import me.ifmo.backend.services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        return ResponseEntity.ok(courseService.getActiveCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }
}
