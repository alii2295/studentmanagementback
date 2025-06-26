package com.example.studentmanagement.services;

import com.example.studentmanagement.models.Student;

import com.example.studentmanagement.respositories.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
}
