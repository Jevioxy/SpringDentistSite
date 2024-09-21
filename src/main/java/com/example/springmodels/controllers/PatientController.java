package com.example.springmodels.controllers;

import com.example.springmodels.models.MedicalRecord;
import com.example.springmodels.models.Patient;
import com.example.springmodels.models.Prescription;
import com.example.springmodels.repos.PatientRepository;
import com.example.springmodels.repos.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patients/")
public class PatientController {
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;


    @Autowired
    public PatientController(PatientRepository patientRepository, PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @GetMapping
    public String getPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        return "all-patients";
    }

    @GetMapping("/create-patient")
    public String createPatientForm(Model model) {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("patient", new Patient());
        return "create-patient";
    }

    @PostMapping("/create-patient")
    public String createPatient(
            @Valid @ModelAttribute("patient") Patient patient,
            @RequestParam("prescriptionId") int prescriptionId,
            BindingResult result) {
        if (result.hasErrors()) {
            return "create-patient";
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);
        if (prescription == null) {
            return "error-page"; // если рецепт не найден
        }

        patient.setPrescription(prescription); // Установите рецепт пациенту

        patientRepository.save(patient);
        return "redirect:/patients/";
    }


    @GetMapping("/{id}")
    public String viewPatient(@PathVariable("id") int id, Model model) {
        Patient patient = patientRepository.findById(id).orElse(null);
        List<Prescription> prescriptions = prescriptionRepository.findAll();

        if (patient == null) {
            return "error-page";
        }
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("patient", patient);
        return "show-patient";
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("patient") Patient patient,
            @RequestParam("prescriptionId") int prescriptionId, // Айди рецепта
            BindingResult result) {
        if (result.hasErrors()) {
            return "edit-patient";
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);
        if (prescription == null) {
            return "error-page"; //  если рецепт не найден
        }

        patient.setPrescription(prescription); //  рецепт пациенту

        patient.setId(id); //  ID для обновления
        patientRepository.save(patient);

        return "redirect:/patients/";
    }

    @GetMapping("/{id}/edit")
    public String editPatientForm(@PathVariable("id") int id, Model model) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return "error-page";
        }
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("patient", patient);
        return "edit-patient";
    }

    @GetMapping("/{id}/delete")
    public String deletePatient(@PathVariable("id") int id) {
        patientRepository.deleteById(id);
        return "redirect:/patients/";
    }
}
