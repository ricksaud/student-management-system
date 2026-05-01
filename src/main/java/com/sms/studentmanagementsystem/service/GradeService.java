package com.sms.studentmanagementsystem.service;

import com.sms.studentmanagementsystem.model.Grade;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getAllGrades(User owner) {
        return gradeRepository.findByOwner(owner);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElse(null);
    }

    public List<Grade> searchGrades(User owner, String keyword) {
        return gradeRepository
                .findByOwnerAndStudent_NameContainingIgnoreCaseOrOwnerAndModule_ModuleNameContainingIgnoreCase(
                        owner, keyword, owner, keyword
                );
    }

    public List<Grade> getRecentGrades(User owner) {
        return gradeRepository.findTop5ByOwnerOrderByIdDesc(owner);
    }

    public List<Grade> getGradesByStudentId(User owner, Long studentId) {
        return gradeRepository.findByOwnerAndStudent_Id(owner, studentId);
    }

    public List<Grade> getGradesByModuleId(User owner, Long moduleId) {
        return gradeRepository.findByOwnerAndModule_Id(owner, moduleId);
    }

    public double getAverageScore(User owner) {
        List<Grade> grades = gradeRepository.findByOwner(owner);
        if (grades.isEmpty()) return 0;

        double total = 0;
        for (Grade grade : grades) {
            total += grade.getScore();
        }
        return total / grades.size();
    }

    public long getPassCount(User owner) {
        return gradeRepository.findByOwner(owner)
                .stream()
                .filter(grade -> grade.getScore() >= 40)
                .count();
    }

    public long getFailCount(User owner) {
        return gradeRepository.findByOwner(owner)
                .stream()
                .filter(grade -> grade.getScore() < 40)
                .count();
    }

    public double getAverageScoreByStudentId(User owner, Long studentId) {
        List<Grade> grades = gradeRepository.findByOwnerAndStudent_Id(owner, studentId);
        if (grades.isEmpty()) return 0;

        double total = 0;
        for (Grade grade : grades) {
            total += grade.getScore();
        }
        return total / grades.size();
    }

    public long getPassCountByStudentId(User owner, Long studentId) {
        return gradeRepository.findByOwnerAndStudent_Id(owner, studentId)
                .stream()
                .filter(grade -> grade.getScore() >= 40)
                .count();
    }

    public long getFailCountByStudentId(User owner, Long studentId) {
        return gradeRepository.findByOwnerAndStudent_Id(owner, studentId)
                .stream()
                .filter(grade -> grade.getScore() < 40)
                .count();
    }

    public int getModuleCountByStudentId(User owner, Long studentId) {
        return gradeRepository.findByOwnerAndStudent_Id(owner, studentId).size();
    }

    public double getAverageScoreByModuleId(User owner, Long moduleId) {
        List<Grade> grades = gradeRepository.findByOwnerAndModule_Id(owner, moduleId);
        if (grades.isEmpty()) return 0;

        double total = 0;
        for (Grade grade : grades) {
            total += grade.getScore();
        }
        return total / grades.size();
    }

    public long getPassCountByModuleId(User owner, Long moduleId) {
        return gradeRepository.findByOwnerAndModule_Id(owner, moduleId)
                .stream()
                .filter(grade -> grade.getScore() >= 40)
                .count();
    }

    public long getFailCountByModuleId(User owner, Long moduleId) {
        return gradeRepository.findByOwnerAndModule_Id(owner, moduleId)
                .stream()
                .filter(grade -> grade.getScore() < 40)
                .count();
    }

    public int getStudentCountByModuleId(User owner, Long moduleId) {
        return gradeRepository.findByOwnerAndModule_Id(owner, moduleId).size();
    }
}