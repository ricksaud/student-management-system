package com.sms.studentmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Module name is required")
    private String moduleName;

    @NotBlank(message = "Module code is required")
    private String moduleCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Module() {
    }

    public Module(String moduleName, String moduleCode) {
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}