package com.sms.studentmanagementsystem.repository;

import com.sms.studentmanagementsystem.model.Student;
import com.sms.studentmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByOwner(User owner);

    List<Student> findTop5ByOwnerOrderByIdDesc(User owner);

    List<Student> findByOwnerAndNameContainingIgnoreCase(User owner, String name);

    boolean existsByEmailAndOwner(String email, User owner);
}