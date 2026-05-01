package com.sms.studentmanagementsystem.repository;

import com.sms.studentmanagementsystem.model.Grade;
import com.sms.studentmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByOwner(User owner);

    List<Grade> findTop5ByOwnerOrderByIdDesc(User owner);

    List<Grade> findByOwnerAndStudent_NameContainingIgnoreCaseOrOwnerAndModule_ModuleNameContainingIgnoreCase(
            User owner1, String studentName, User owner2, String moduleName
    );

    List<Grade> findByOwnerAndStudent_Id(User owner, Long studentId);

    List<Grade> findByOwnerAndModule_Id(User owner, Long moduleId);
}