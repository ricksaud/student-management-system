package com.sms.studentmanagementsystem.service;

import com.sms.studentmanagementsystem.model.Module;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Module saveModule(Module module) {
        return moduleRepository.save(module);
    }

    public List<Module> getAllModules(User owner) {
        return moduleRepository.findByOwner(owner);
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public List<Module> searchModules(User owner, String keyword) {
        return moduleRepository
                .findByOwnerAndModuleNameContainingIgnoreCaseOrOwnerAndModuleCodeContainingIgnoreCase(
                        owner, keyword, owner, keyword
                );
    }

    public List<Module> getRecentModules(User owner) {
        return moduleRepository.findTop5ByOwnerOrderByIdDesc(owner);
    }

    public boolean moduleCodeExists(String moduleCode, User owner) {
        return moduleRepository.existsByModuleCodeAndOwner(moduleCode, owner);
    }

    public boolean moduleCodeExistsForAnotherModule(String moduleCode, Long id, User owner) {
        List<Module> modules = moduleRepository.findByOwner(owner);
        for (Module module : modules) {
            if (module.getModuleCode().equalsIgnoreCase(moduleCode) && !module.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}