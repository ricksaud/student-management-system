package com.sms.studentmanagementsystem.repository;

import com.sms.studentmanagementsystem.model.Module;
import com.sms.studentmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByOwner(User owner);

    List<Module> findTop5ByOwnerOrderByIdDesc(User owner);

    List<Module> findByOwnerAndModuleNameContainingIgnoreCaseOrOwnerAndModuleCodeContainingIgnoreCase(
            User owner1, String moduleName, User owner2, String moduleCode
    );

    boolean existsByModuleCodeAndOwner(String moduleCode, User owner);
}