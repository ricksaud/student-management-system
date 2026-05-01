package com.sms.studentmanagementsystem.controller;

import com.sms.studentmanagementsystem.model.Module;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.service.GradeService;
import com.sms.studentmanagementsystem.service.ModuleService;
import com.sms.studentmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;
    private final GradeService gradeService;
    private final UserService userService;

    public ModuleController(ModuleService moduleService,
                            GradeService gradeService,
                            UserService userService) {
        this.moduleService = moduleService;
        this.gradeService = gradeService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping("/page")
    public String showModulePage(Model model, Authentication authentication) {
        User owner = getCurrentUser(authentication);

        model.addAttribute("module", new Module());
        model.addAttribute("modules", moduleService.getAllModules(owner));
        return "modules";
    }

    @PostMapping("/save")
    public String saveModule(@Valid @ModelAttribute("module") Module module,
                             BindingResult result,
                             Model model,
                             Authentication authentication) {

        User owner = getCurrentUser(authentication);

        if (module.getId() == null) {
            if (moduleService.moduleCodeExists(module.getModuleCode(), owner)) {
                result.rejectValue("moduleCode", "error.module", "Module code already exists");
            }
        } else {
            if (moduleService.moduleCodeExistsForAnotherModule(module.getModuleCode(), module.getId(), owner)) {
                result.rejectValue("moduleCode", "error.module", "Module code already exists");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("modules", moduleService.getAllModules(owner));
            return "modules";
        }

        module.setOwner(owner);
        moduleService.saveModule(module);

        return "redirect:/modules/page?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteModule(@PathVariable Long id,
                               Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Module module = moduleService.getModuleById(id);

        if (moduleDoesNotBelongToUser(module, owner)) {
            return "redirect:/modules/page";
        }

        moduleService.deleteModule(id);
        return "redirect:/modules/page?deleted";
    }

    @GetMapping("/edit/{id}")
    public String editModule(@PathVariable Long id,
                             Model model,
                             Authentication authentication) {

        User owner = getCurrentUser(authentication);
        Module module = moduleService.getModuleById(id);

        if (moduleDoesNotBelongToUser(module, owner)) {
            return "redirect:/modules/page";
        }

        model.addAttribute("module", module);
        model.addAttribute("modules", moduleService.getAllModules(owner));
        return "modules";
    }

    @GetMapping("/search")
    public String searchModules(@RequestParam("keyword") String keyword,
                                Model model,
                                Authentication authentication) {

        User owner = getCurrentUser(authentication);

        model.addAttribute("module", new Module());
        model.addAttribute("modules", moduleService.searchModules(owner, keyword));
        return "modules";
    }

    @GetMapping("/view/{id}")
    public String viewModuleDetails(@PathVariable Long id,
                                    Model model,
                                    Authentication authentication) {

        User owner = getCurrentUser(authentication);

        Module module = moduleService.getModuleById(id);

        model.addAttribute("moduleDetails", module);
        model.addAttribute("moduleGrades", gradeService.getGradesByModuleId(owner, id));
        model.addAttribute("averageScore", String.format("%.2f", gradeService.getAverageScoreByModuleId(owner, id)));
        model.addAttribute("passCount", gradeService.getPassCountByModuleId(owner, id));
        model.addAttribute("failCount", gradeService.getFailCountByModuleId(owner, id));
        model.addAttribute("studentCount", gradeService.getStudentCountByModuleId(owner, id));

        return "module-details";
    }

    private boolean moduleDoesNotBelongToUser(Module module, User owner) {
        return module == null || module.getOwner() == null || !module.getOwner().getId().equals(owner.getId());
    }
}