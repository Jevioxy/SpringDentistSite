package com.example.springmodels.controllers;

import com.example.springmodels.models.Dentist;
import com.example.springmodels.repos.DentistRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dentists/")
public class DentistController {
    private final DentistRepository dentistRepository;

    @Autowired
    public DentistController(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @GetMapping
    public String getDentists(Model model) {
        List<Dentist> dentists = dentistRepository.findAll();
        model.addAttribute("dentists", dentists);
        return "all-dentists";
    }

    @GetMapping("/create-dentist")
    public String createDentistForm(Model model) {
        model.addAttribute("dentist", new Dentist());
        return "create-dentist";
    }

    @PostMapping("/create-dentist")
    public String createDentist(@Valid @ModelAttribute("dentist") Dentist dentist, BindingResult result) {
        if (result.hasErrors()) {
            return "create-dentist";
        }
        dentistRepository.save(dentist);
        return "redirect:/dentists/";
    }

    @GetMapping("/{id}")
    public String viewDentist(@PathVariable("id") int id, Model model) {
        Dentist dentist = dentistRepository.findById(id).orElse(null);
        if (dentist == null) {
            return "error-page";
        }
        model.addAttribute("dentist", dentist);
        return "show-dentist";
    }

    @PostMapping("/{id}/edit")
    public String updateDentist(@PathVariable("id") int id, @Valid @ModelAttribute("dentist") Dentist dentist, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-dentist"; // возроащаем ошибки валидации
        }
        dentist.setId(id);
        dentistRepository.save(dentist);
        return "redirect:/dentists/";
    }

    @GetMapping("/{id}/edit")
    public String editDentistForm(@PathVariable("id") int id, Model model) {
        Dentist dentist = dentistRepository.findById(id).orElse(null);
        if (dentist == null) {
            return "error-page";
        }
        model.addAttribute("dentist", dentist);
        return "edit-dentist";
    }

    @GetMapping("/{id}/delete")
    public String deleteDentist(@PathVariable("id") int id) {
        dentistRepository.deleteById(id);
        return "redirect:/dentists/";
    }
}
