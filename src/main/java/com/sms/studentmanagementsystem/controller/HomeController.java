package com.sms.studentmanagementsystem.controller;

import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.service.GradeService;
import com.sms.studentmanagementsystem.service.ModuleService;
import com.sms.studentmanagementsystem.service.StudentService;
import com.sms.studentmanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudentService studentService;
    private final ModuleService moduleService;
    private final GradeService gradeService;
    private final UserService userService;

    public HomeController(StudentService studentService,
                          ModuleService moduleService,
                          GradeService gradeService,
                          UserService userService) {
        this.studentService = studentService;
        this.moduleService = moduleService;
        this.gradeService = gradeService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        User owner = getCurrentUser(authentication);

        int studentCount = studentService.getAllStudents(owner).size();
        int moduleCount = moduleService.getAllModules(owner).size();
        int gradeCount = gradeService.getAllGrades(owner).size();
        long passCount = gradeService.getPassCount(owner);
        long failCount = gradeService.getFailCount(owner);

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("moduleCount", moduleCount);
        model.addAttribute("gradeCount", gradeCount);
        model.addAttribute("averageGrade", String.format("%.2f", gradeService.getAverageScore(owner)));
        model.addAttribute("passCount", passCount);
        model.addAttribute("failCount", failCount);

        model.addAttribute("studentCountNumber", studentCount);
        model.addAttribute("moduleCountNumber", moduleCount);
        model.addAttribute("gradeCountNumber", gradeCount);
        model.addAttribute("passCountNumber", passCount);
        model.addAttribute("failCountNumber", failCount);

        model.addAttribute("recentStudents", studentService.getRecentStudents(owner));
        model.addAttribute("recentModules", moduleService.getRecentModules(owner));
        model.addAttribute("recentGrades", gradeService.getRecentGrades(owner));

        return "index";
    }
}