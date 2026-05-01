package com.sms.studentmanagementsystem.service;

import com.sms.studentmanagementsystem.model.Student;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(User owner) {
        return studentRepository.findByOwner(owner);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> searchStudentsByName(User owner, String name) {
        return studentRepository.findByOwnerAndNameContainingIgnoreCase(owner, name);
    }

    public List<Student> getRecentStudents(User owner) {
        return studentRepository.findTop5ByOwnerOrderByIdDesc(owner);
    }

    public boolean emailExists(String email, User owner) {
        return studentRepository.existsByEmailAndOwner(email, owner);
    }

    public boolean emailExistsForAnotherStudent(String email, Long id, User owner) {
        List<Student> students = studentRepository.findByOwner(owner);
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(email) && !student.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}