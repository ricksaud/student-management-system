package com.sms.studentmanagementsystem.controller;

import com.sms.studentmanagementsystem.model.Student;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.service.GradeService;
import com.sms.studentmanagementsystem.service.StudentService;
import com.sms.studentmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;
    private final UserService userService;

    public StudentController(StudentService studentService,
                             GradeService gradeService,
                             UserService userService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping("/page")
    public String showStudentPage(Model model, Authentication authentication) {
        User owner = getCurrentUser(authentication);

        model.addAttribute("student", new Student());
        model.addAttribute("students", studentService.getAllStudents(owner));
        return "students";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              Model model,
                              Authentication authentication) {

        User owner = getCurrentUser(authentication);

        if (student.getId() == null) {
            if (studentService.emailExists(student.getEmail(), owner)) {
                result.rejectValue("email", "error.student", "Email already exists");
            }
        } else {
            if (studentService.emailExistsForAnotherStudent(student.getEmail(), student.getId(), owner)) {
                result.rejectValue("email", "error.student", "Email already exists");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents(owner));
            return "students";
        }

        student.setOwner(owner);
        studentService.saveStudent(student);

        return "redirect:/students/page?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id,
                                Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Student student = studentService.getStudentById(id);

        if (studentDoesNotBelongToUser(student, owner)) {
            return "redirect:/students/page";
        }

        studentService.deleteStudent(id);
        return "redirect:/students/page?deleted";
    }
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id,
                              Model model,
                              Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Student student = studentService.getStudentById(id);

        if (studentDoesNotBelongToUser(student, owner)) {
            return "redirect:/students/page";
        }

        model.addAttribute("student", student);
        model.addAttribute("students", studentService.getAllStudents(owner));
        return "students";
    }

    @GetMapping("/search")
    public String searchStudents(@RequestParam("keyword") String keyword,
                                 Model model,
                                 Authentication authentication) {

        User owner = getCurrentUser(authentication);

        model.addAttribute("student", new Student());
        model.addAttribute("students", studentService.searchStudentsByName(owner, keyword));
        return "students";
    }

    @GetMapping("/view/{id}")
    public String viewStudentDetails(@PathVariable Long id,
                                     Model model,
                                     Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Student student = studentService.getStudentById(id);

        if (studentDoesNotBelongToUser(student, owner)) {
            return "redirect:/students/page";
        }

        model.addAttribute("studentDetails", student);
        model.addAttribute("studentGrades", gradeService.getGradesByStudentId(owner, id));
        model.addAttribute("averageScore", String.format("%.2f", gradeService.getAverageScoreByStudentId(owner, id)));
        model.addAttribute("passCount", gradeService.getPassCountByStudentId(owner, id));
        model.addAttribute("failCount", gradeService.getFailCountByStudentId(owner, id));
        model.addAttribute("moduleCount", gradeService.getModuleCountByStudentId(owner, id));

        return "student-details";
    }
    private boolean studentDoesNotBelongToUser(Student student, User owner) {
        return student == null || student.getOwner() == null || !student.getOwner().getId().equals(owner.getId());
    }
}