package me.ifmo.backend.repositories;

import me.ifmo.backend.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
           SELECT c FROM Course c
           WHERE c.active = true
           ORDER BY c.id ASC
           """)
    List<Course> findActiveCoursesOrdered();
}
