package com.example.studentmanagement.services;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;

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

    public Student updateStudent(Long id, Student student) {
        student.setId(id);
        return repo.save(student);
    }

    public void deleteStudent(Long id) {
        repo.deleteById(id);
    }
}
