package com.example.studentmanagement.services;

import com.example.studentmanagement.models.Student;

import com.example.studentmanagement.respositories.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;
    private final Path uploadDir = Paths.get("uploads/");


    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public Student getStudentById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return repo.save(student);
    }

    public Student updateStudent(Long id, Student updated) {
        Student student = repo.findById(id).orElseThrow();
        student.setFirstName(updated.getFirstName());
        student.setLastName(updated.getLastName());
        student.setEmail(updated.getEmail());
        student.setDepartment(updated.getDepartment());
        student.setPhotoUrl(updated.getPhotoUrl());
        return repo.save(student);
    }

    public void deleteStudent(Long id) {
        repo.deleteById(id);
    }
    public List<Student> search(String keyword) {
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword);
    }
    public List<Student> getSortedStudents(String sortBy) {
        return repo.findAll(Sort.by(Sort.Direction.ASC, sortBy.trim()));
    }
    public Page<Student> getPaginatedStudents(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }
    // ✅ Upload photo (déléguer au service)
    public String uploadPhoto(Long id, MultipartFile file) throws IOException {
        Files.createDirectories(uploadDir);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = uploadDir.resolve(filename);
        Files.write(path, file.getBytes());

        Student student = getStudentById(id);
        student.setPhotoUrl("/api/students/photo/" + filename);
        repo.save(student);

        return filename;
    }
    // ✅ Récupérer la ressource image
    public Resource loadPhoto(String filename) throws IOException {
        Path path = uploadDir.resolve(filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new IOException("File not found: " + filename);
        }

        return resource;
    }

}

