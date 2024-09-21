package com.example.springmodels.controllers;

import com.example.springmodels.models.MedicalRecord;
import com.example.springmodels.models.Patient;
import com.example.springmodels.repos.MedicalRecordRepository;
import com.example.springmodels.repos.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@PreAuthorize("hasAnyAuthority('DENTIST')")
@RequestMapping("/medical-records/")
public class MedicalRecordController {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public String getMedicalRecords(Model model) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll(); // получаем мед. записей
        model.addAttribute("medicalRecords", medicalRecords);
        List<Patient> patients = patientRepository.findAll(); // получаем список пациентов
        model.addAttribute("patients", patients); // добавляем список пациентов в модель
        return "all-medical-records";
    }

    @GetMapping("/create")
    public String createMedicalRecordForm(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        model.addAttribute("medicalRecord", new MedicalRecord());
        return "create-medical-record";
    }

    @PostMapping("/create")
    public String createMedicalRecord(
            @Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
            @RequestParam("patientId") int patientId,
            BindingResult result) {
        if (result.hasErrors()) {
            return "create-medical-record";
        }
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return "error-page";
        }
        medicalRecord.setPatient(patient);

        medicalRecordRepository.save(medicalRecord);
        return "redirect:/medical-records/";
    }

    @GetMapping("/{id}")
    public String viewMedicalRecord(@PathVariable("id") int id, Model model) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id).orElse(null);
        List<Patient> patients = patientRepository.findAll(); // получаем список пациентов
        if (medicalRecord == null) {
            return "error-page";
        }
        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("patients", patients); // добавляем список пациентов в модель
        return "show-medical-record";
    }

    @GetMapping("/{id}/edit")
    public String editMedicalRecordForm(@PathVariable("id") int id, Model model) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id).orElse(null);
        if (medicalRecord == null) {
            return "error-page";}
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("patients", patients);
        return "edit-medical-record";
    }

    @PostMapping("/{id}/edit")
    public String updateMedicalRecord(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("medicalRecord") MedicalRecord medicalRecord,
            @RequestParam("patientId") int patientId,  // айди выбранного пациента
            BindingResult result) {
        if (result.hasErrors()) {
            return "edit-medical-record";
        }
        Patient patient = patientRepository.findById(patientId).orElse(null);

        if (patient == null) {
            return "error-page";
        }

        medicalRecord.setPatient(patient);
        medicalRecord.setId(id);
        medicalRecordRepository.save(medicalRecord);
        return "redirect:/medical-records/";
    }

    @GetMapping("/{id}/delete")
    public String deleteMedicalRecord(@PathVariable("id") int id) {
        medicalRecordRepository.deleteById(id);
        return "redirect:/medical-records/";
    }
}
