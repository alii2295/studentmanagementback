package com.example.studentmanagement.controllers;

import com.example.studentmanagement.models.Student;
import com.example.studentmanagement.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return service.getStudentById(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return service.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteStudent(id);
    }
    @GetMapping("/search")
    public List<Student> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
    @GetMapping("/sorted")
    public List<Student> getSortedStudents(@RequestParam(defaultValue = "firstName") String sortBy) {
        return service.getSortedStudents(sortBy);
    }
    @GetMapping("/paginated")
    public Page<Student> getStudentsPaginated(@RequestParam int page, @RequestParam int size) {
        return service.getPaginatedStudents(page, size);
    }

    // ✅ Upload photo
    @PostMapping("/upload-photo/{id}")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String filename = service.uploadPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded: " + filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }

    // ✅ Télécharger photo
    @GetMapping("/photo/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Resource file = service.loadPhoto(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}




