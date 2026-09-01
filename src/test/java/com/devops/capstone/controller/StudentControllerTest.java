package com.devops.capstone.controller;

import com.devops.capstone.model.Student;
import com.devops.capstone.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository repository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/students";
        repository.deleteAll();
        repository.save(new Student("SV001", "Nguyen Van A", 20, "K20"));
    }

    @Test
    void testGetAllStudents() {
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("success"));
        assertNotNull(body.get("data"));
    }

    @Test
    void testGetStudentById() {
        Student saved = repository.findAll().get(0);
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/" + saved.getId(), Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testStudentNotFound() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/9999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateStudent() {
        Student newStudent = new Student("SV004", "Pham Thi D", 22, "K19");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(newStudent, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateStudentMissingName() {
        Student invalid = new Student("SV005", null, 20, "K20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(invalid, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHealthEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/health", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    void testSearch() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/search?q=Nguyen", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertTrue((int) body.get("count") >= 1);
    }
}