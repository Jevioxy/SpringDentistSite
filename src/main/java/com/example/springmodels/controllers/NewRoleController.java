package com.example.springmodels.controllers;

import com.example.springmodels.models.modelUser;
import com.example.springmodels.models.roleEnum;
import com.example.springmodels.repos.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PreAuthorize("hasAnyAuthority('DENTIST')")
public class NewRoleController {
    @Autowired
    private userRepository userRepository;

    @GetMapping("/add-role")
    public String getAdd(Model model) {
        Iterable<modelUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roleEnum.values());
        return "add-role";
    }
}
