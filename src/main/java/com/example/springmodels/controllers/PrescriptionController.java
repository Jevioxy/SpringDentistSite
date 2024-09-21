package com.example.springmodels.controllers;


import com.example.springmodels.models.Dentist;
import com.example.springmodels.models.Prescription;
import com.example.springmodels.repos.DentistRepository;
import com.example.springmodels.repos.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prescriptions/")
public class PrescriptionController {
    private final PrescriptionRepository prescriptionRepository;
    private final DentistRepository dentistRepository;

    @Autowired
    public PrescriptionController(PrescriptionRepository prescriptionRepository, DentistRepository dentistRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.dentistRepository = dentistRepository;
    }

    @GetMapping
    public String getPrescriptions(Model model) {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        List<Dentist> dentists = dentistRepository.findAll();
        model.addAttribute("dentists", dentists);
        return "all-prescriptions";
    }

    @GetMapping("/create")
    public String createPrescriptionForm(Model model) {
        List<Dentist> dentists = dentistRepository.findAll();
        model.addAttribute("dentists", dentists);
        model.addAttribute("prescription", new Prescription());
        return "create-prescription";
    }

    @PostMapping("/create")
    public String createPrescription(
            @Valid @ModelAttribute("prescription") Prescription prescription,
            @RequestParam("dentistId") int dentistId,  // получаем айди  выбранного стоматолога
            BindingResult result) {
        if (result.hasErrors()) {
            return "create-prescription";
        }
        Dentist dentist = dentistRepository.findById(dentistId).orElse(null);

        if (dentist == null) {
            return "error-page";
        }
        // установим стоматолога в Prescription
        prescription.setDentist(dentist);
        prescriptionRepository.save(prescription);
        return "redirect:/prescriptions/";
    }

    @GetMapping("/{id}")
    public String viewPrescription(@PathVariable("id") int id, Model model) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        List<Dentist> dentists = dentistRepository.findAll();
        if (prescription == null) {
            return "error-page";
        }
        model.addAttribute("prescription", prescription);
        model.addAttribute("dentists", dentists);
        return "show-prescription";
    }

    @GetMapping("/{id}/edit")
    public String editPrescriptionForm(@PathVariable("id") int id, Model model) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        if (prescription == null) {
            return "error-page";
        }
        List<Dentist> dentists = dentistRepository.findAll();
        model.addAttribute("prescription", prescription);
        model.addAttribute("dentists", dentists);
        return "edit-prescription";
    }

    @PostMapping("/{id}/edit")
    public String updatePrescription(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("prescription") Prescription prescription,
            @RequestParam("dentistId") int dentistId,  // id выбранного стоматолога
            BindingResult result) {
        if (result.hasErrors()) {
            return "edit-prescription";
        }

        Dentist dentist = dentistRepository.findById(dentistId).orElse(null);

        if (dentist == null) {
            return "error-page";
        }
        prescription.setDentist(dentist);
        prescription.setId(id);
        prescriptionRepository.save(prescription);
        return "redirect:/prescriptions/";
    }

    @GetMapping("/{id}/delete")
    public String deletePrescription(@PathVariable("id") int id) {
        prescriptionRepository.deleteById(id);
        return "redirect:/prescriptions/";
    }
}