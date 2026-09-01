package com.devops.capstone;

import com.devops.capstone.model.Student;
import com.devops.capstone.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagerApplication implements CommandLineRunner {

    private final StudentRepository repository;

    public StudentManagerApplication(StudentRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(StudentManagerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Seed data
        repository.save(new Student("SV001", "Nguyen Van A", 20, "K20"));
        repository.save(new Student("SV002", "Tran Thi B", 21, "K20"));
        repository.save(new Student("SV003", "Le Van C", 19, "K21"));
        System.out.println("Student Manager API ready — " + repository.count() + " students seeded");
    }
}