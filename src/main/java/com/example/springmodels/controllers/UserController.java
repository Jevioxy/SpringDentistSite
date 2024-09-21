package com.example.springmodels.controllers;

import com.example.springmodels.models.modelUser;
import com.example.springmodels.models.roleEnum;
import com.example.springmodels.repos.userRepository;  // правильное имя репозитория
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {

    @Autowired
    private userRepository userRepository;  // правильное имя

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Методы для Thymeleaf (рендеринг страниц)

    @GetMapping("/users")
    public String userView(Model model) {
        Iterable<modelUser> userList = userRepository.findAll();
        model.addAttribute("user_list", userList);
        return "admin/users";
    }

    @GetMapping("/{id}")
    public String detailView(@PathVariable Long id, Model model) {
        model.addAttribute("user_object", userRepository.findById(id).orElseThrow());
        return "admin/info";
    }

    @GetMapping("/{id}/update")
    public String updView(@PathVariable Long id, Model model) {
        model.addAttribute("user_object", userRepository.findById(id).orElseThrow());
        model.addAttribute("roles", roleEnum.values());
        return "admin/update";
    }

    @PostMapping("/{id}/update")
    public String update_user(@RequestParam String username,
                              @RequestParam(name = "roles[]", required = false) String[] roles,
                              @PathVariable Long id) {
        modelUser user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);

        user.getRoles().clear();
        if (roles != null) {
            for (String role : roles) {
                user.getRoles().add(roleEnum.valueOf(role));
            }
        }

        userRepository.save(user);
        return "redirect:/admin/{id}";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
