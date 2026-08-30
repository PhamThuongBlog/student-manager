package com.devops.capstone.repository;

import com.devops.capstone.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContainingIgnoreCase(String name);
    Student findByStudentId(String studentId);
}