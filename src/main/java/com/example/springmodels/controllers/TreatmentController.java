package com.example.springmodels.controllers;

import com.example.springmodels.models.Treatment;
import com.example.springmodels.repos.TreatmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/treatments/")
public class TreatmentController {
    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentController(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @GetMapping
    public String getTreatments(Model model) {
        List<Treatment> treatments = treatmentRepository.findAll();
        model.addAttribute("treatments", treatments);
        return "all-treatments";
    }

    @GetMapping("/create-treatment")
    public String createTreatmentForm(Model model) {
        model.addAttribute("treatment", new Treatment());
        return "create-treatment";
    }

    @PostMapping("/create-treatment")
    public String createTreatment(@Valid @ModelAttribute("treatment") Treatment treatment, BindingResult result) {
        if (result.hasErrors()) {
            return "create-treatment";
        }
        treatmentRepository.save(treatment);
        return "redirect:/treatments/";
    }

    @GetMapping("/{id}")
    public String viewTreatment(@PathVariable("id") int id, Model model) {
        Treatment treatment = treatmentRepository.findById(id).orElse(null);
        if (treatment == null) {
            return "error-page";
        }
        model.addAttribute("treatment", treatment);
        return "show-treatment";
    }

    @GetMapping("/{id}/edit")
    public String editTreatmentForm(@PathVariable("id") int id, Model model) {
        Treatment treatment = treatmentRepository.findById(id).orElse(null);
        if (treatment == null) {
            return "error-page";
        }
        model.addAttribute("treatment", treatment);
        return "edit-treatment";
    }

    @PostMapping("/{id}/edit")
    public String updateTreatment(@PathVariable("id") int id, @Valid @ModelAttribute("treatment") Treatment treatment, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-treatment";
        }
        treatment.setId(id);
        treatmentRepository.save(treatment);
        return "redirect:/treatments/";
    }

    @GetMapping("/{id}/delete")
    public String deleteTreatment(@PathVariable("id") int id) {
        treatmentRepository.deleteById(id);
        return "redirect:/treatments/";
    }
}
