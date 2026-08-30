package com.devops.capstone.controller;

import com.devops.capstone.model.Student;
import com.devops.capstone.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    // GET all students
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        List<Student> students = repository.findAll();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", students);
        response.put("total", students.size());
        response.put("service", "student-manager");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    // GET student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudent(@PathVariable Long id) {
        Optional<Student> student = repository.findById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        if (student.isPresent()) {
            response.put("success", true);
            response.put("data", student.get());
        } else {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // POST create student
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
        Map<String, Object> response = new LinkedHashMap<>();
        if (student.getName() == null || student.getStudentId() == null) {
            response.put("success", false);
            response.put("message", "Name and studentId are required");
            return ResponseEntity.badRequest().body(response);
        }
        Student saved = repository.save(student);
        response.put("success", true);
        response.put("data", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE student
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        if (!repository.existsById(id)) {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        repository.deleteById(id);
        response.put("success", true);
        response.put("message", "Student deleted");
        return ResponseEntity.ok(response);
    }

    // GET search
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam String q) {
        List<Student> results = repository.findByNameContainingIgnoreCase(q);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", results);
        response.put("keyword", q);
        response.put("count", results.size());
        return ResponseEntity.ok(response);
    }

    // GET health
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "student-manager");
        response.put("version", "1.0.0");
        response.put("timestamp", new Date().toString());
        return ResponseEntity.ok(response);
    }
}