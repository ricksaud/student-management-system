package com.sms.studentmanagementsystem.controller;

import com.sms.studentmanagementsystem.model.Grade;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.service.GradeService;
import com.sms.studentmanagementsystem.service.ModuleService;
import com.sms.studentmanagementsystem.service.StudentService;
import com.sms.studentmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;
    private final StudentService studentService;
    private final ModuleService moduleService;
    private final UserService userService;

    public GradeController(GradeService gradeService,
                           StudentService studentService,
                           ModuleService moduleService,
                           UserService userService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.moduleService = moduleService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping("/page")
    public String showGradePage(Model model, Authentication authentication) {
        User owner = getCurrentUser(authentication);

        model.addAttribute("grade", new Grade());
        model.addAttribute("grades", gradeService.getAllGrades(owner));
        model.addAttribute("students", studentService.getAllStudents(owner));
        model.addAttribute("modules", moduleService.getAllModules(owner));
        return "grades";
    }

    @PostMapping("/save")
    public String saveGrade(@Valid @ModelAttribute("grade") Grade grade,
                            BindingResult result,
                            Model model,
                            Authentication authentication) {

        User owner = getCurrentUser(authentication);

        if (result.hasErrors()) {
            model.addAttribute("grades", gradeService.getAllGrades(owner));
            model.addAttribute("students", studentService.getAllStudents(owner));
            model.addAttribute("modules", moduleService.getAllModules(owner));
            return "grades";
        }

        grade.setOwner(owner);
        gradeService.saveGrade(grade);

        return "redirect:/grades/page?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteGrade(@PathVariable Long id,
                              Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Grade grade = gradeService.getGradeById(id);

        if (gradeDoesNotBelongToUser(grade, owner)) {
            return "redirect:/grades/page";
        }

        gradeService.deleteGrade(id);
        return "redirect:/grades/page?deleted";
    }

    @GetMapping("/edit/{id}")
    public String editGrade(@PathVariable Long id,
                            Model model,
                            Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Grade grade = gradeService.getGradeById(id);

        if (gradeDoesNotBelongToUser(grade, owner)) {
            return "redirect:/grades/page";
        }

        model.addAttribute("grade", grade);
        model.addAttribute("grades", gradeService.getAllGrades(owner));
        model.addAttribute("students", studentService.getAllStudents(owner));
        model.addAttribute("modules", moduleService.getAllModules(owner));
        return "grades";
    }

    @GetMapping("/search")
    public String searchGrades(@RequestParam("keyword") String keyword,
                               Model model,
                               Authentication authentication) {

        User owner = getCurrentUser(authentication);

        model.addAttribute("grade", new Grade());
        model.addAttribute("grades", gradeService.searchGrades(owner, keyword));
        model.addAttribute("students", studentService.getAllStudents(owner));
        model.addAttribute("modules", moduleService.getAllModules(owner));
        return "grades";
    }

    private boolean gradeDoesNotBelongToUser(Grade grade, User owner) {
        return grade == null || grade.getOwner() == null || !grade.getOwner().getId().equals(owner.getId());
    }
}