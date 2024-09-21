package com.example.springmodels.controllers;

import com.example.springmodels.models.Patient;
import com.example.springmodels.models.PatientTreatment;
import com.example.springmodels.models.Prescription;
import com.example.springmodels.models.Treatment;
import com.example.springmodels.repos.PatientRepository;
import com.example.springmodels.repos.PatientTreatmentRepository;
import com.example.springmodels.repos.PrescriptionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.springmodels.repos.TreatmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@RequestMapping("/patient-treatments/")
public class PatientTreatmentController {
    private final PatientTreatmentRepository patientTreatmentRepository;
    private final PatientRepository patientRepository;
    private final TreatmentRepository treatmentRepository;


    @Autowired
    public PatientTreatmentController(PatientTreatmentRepository patientTreatmentRepository, PatientRepository patientRepository, TreatmentRepository treatmentRepository) {
        this.patientTreatmentRepository = patientTreatmentRepository;
        this.patientRepository = patientRepository;
        this.treatmentRepository = treatmentRepository;
    }

    @GetMapping
    public String getPatientTreatments(Model model) {
        List<PatientTreatment> patientTreatments = patientTreatmentRepository.findAll();
        model.addAttribute("patientTreatments", patientTreatments);
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        List<Treatment> treatments = treatmentRepository.findAll();
        model.addAttribute("treatments", treatments);
        return "all-patient-treatments";
    }

    @GetMapping("/create")
    public String createPatientTreatmentForm(Model model) {
        List<Patient> patients = patientRepository.findAll();
        List<Treatment> treatments = treatmentRepository.findAll();
        model.addAttribute("patients", patients);
        model.addAttribute("treatments", treatments);
        model.addAttribute("patientTreatment", new PatientTreatment());
        return "create-patient-treatment";
    }

    @PostMapping("/create")
    public String createPatientTreatment(
            @ModelAttribute("patientTreatment") PatientTreatment patientTreatment,
            @RequestParam("patientId") int patientId,
            @RequestParam("treatmentId") int treatmentId,
            BindingResult result) {
        if (result.hasErrors()) {
            return "create-patient-treatment";
        }

        Patient patient = patientRepository.findById(patientId).orElse(null);
        Treatment treatment = treatmentRepository.findById(treatmentId).orElse(null);

        if (patient == null || treatment == null) {
            return "error-page";
        }

        patientTreatment.setPatient(patient);
        patientTreatment.setTreatment(treatment);

        patientTreatmentRepository.save(patientTreatment);
        return "redirect:/patient-treatments/";
    }

    @GetMapping("/{id}")
    public String viewPatientTreatment(@PathVariable("id") int id, Model model) {
        PatientTreatment patientTreatment = patientTreatmentRepository.findById(id).orElse(null);
        List<Patient> patients = patientRepository.findAll();
        List<Treatment> treatments = treatmentRepository.findAll();

        if (patientTreatment == null) {
            return "error-page";
        }

        model.addAttribute("patientTreatment", patientTreatment);
        model.addAttribute("patients", patients);
        model.addAttribute("treatments", treatments);
        return "show-patient-treatment";
    }

    @GetMapping("/{id}/edit")
    public String editPatientTreatmentForm(@PathVariable("id") int id, Model model) {
        PatientTreatment patientTreatment = patientTreatmentRepository.findById(id).orElse(null);
        if (patientTreatment == null) {
            return "error-page";
        }

        List<Patient> patients = patientRepository.findAll();
        List<Treatment> treatments = treatmentRepository.findAll();
        model.addAttribute("patientTreatment", patientTreatment);
        model.addAttribute("patients", patients);
        model.addAttribute("treatments", treatments);
        return "edit-patient-treatment";
    }

    @PostMapping("/{id}/edit")
    public String updatePatientTreatment(@PathVariable("id") int id,
                                         @Valid @ModelAttribute("patientTreatment") PatientTreatment patientTreatment,
                                         @RequestParam("patientId") int patientId,
                                         @RequestParam("treatmentId") int treatmentId,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return "edit-patient-treatment";
        }

        Treatment treatment = treatmentRepository.findById(treatmentId).orElse(null);
        Patient patient = patientRepository.findById(patientId).orElse(null);

        if (patient == null) {
            return "error-page";
        }
        if (treatment == null) {
            return "error-page";
        }
        patientTreatment.setPatient(patient);
        patientTreatment.setTreatment(treatment);

        patientTreatment.setId(id);
        patientTreatmentRepository.save(patientTreatment);
        return "redirect:/patient-treatments/";
    }

    @GetMapping("/{id}/delete")
    public String deletePatientTreatment(@PathVariable("id") int id) {
        patientTreatmentRepository.deleteById(id);
        return "redirect:/patient-treatments/";
    }
}
