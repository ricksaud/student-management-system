package com.sms.studentmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must be at most 100")
    private Double score;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Grade() {
    }

    public Grade(Double score, Student student, Module module) {
        this.score = score;
        this.student = student;
        this.module = module;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getClassification() {
        if (score == null) {
            return "";
        } else if (score < 40) {
            return "Fail";
        } else if (score < 60) {
            return "Pass";
        } else if (score < 70) {
            return "Merit";
        } else {
            return "Distinction";
        }
    }
    public String getClassificationClass() {
        if (score == null) {
            return "";
        } else if (score < 40) {
            return "badge-fail";
        } else if (score < 60) {
            return "badge-pass";
        } else if (score < 70) {
            return "badge-merit";
        } else {
            return "badge-distinction";
        }


    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}