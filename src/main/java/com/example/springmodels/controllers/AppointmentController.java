package com.example.springmodels.controllers;

import com.example.springmodels.models.Appointment;
import com.example.springmodels.models.MedicalRecord;
import com.example.springmodels.models.Patient;
import com.example.springmodels.models.Prescription;
import com.example.springmodels.repos.AppointmentRepository;
import com.example.springmodels.repos.DentistRepository;
import com.example.springmodels.repos.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Controller
@RequestMapping("/appointments/")
@PreAuthorize("hasAnyAuthority('USER')")

public class AppointmentController {
    private final AppointmentRepository repositoryClass;
    private final PrescriptionRepository prescriptionRepository;


    @Autowired
    public AppointmentController(AppointmentRepository repositoryClass, PrescriptionRepository prescriptionRepository) {
        this.repositoryClass = repositoryClass;
        this.prescriptionRepository = prescriptionRepository;

    }

    @GetMapping
    public String getAppointments(@RequestParam(name = "description", defaultValue = "") String description, Model model) {
        List<Appointment> appointments;
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);

        if (description.isEmpty()) {
            appointments = repositoryClass.findAll();
        } else {
            appointments = repositoryClass.findByDescription(description);
        }


        model.addAttribute("appointments", appointments);
        return "all-appointments";
    }

    @PostMapping
    public String postAppointment(@RequestParam(name = "description", defaultValue = "") String description, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("description", description);
        return "redirect:/appointments/";
    }

    @GetMapping("/create-appointment")
    public String createAppointmentForm(Model model) {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("appointment", new Appointment());
        return "create-appointment";
    }

    @PostMapping("/create-appointment")
    public String createAppointment(@Valid @ModelAttribute("appointment") Appointment appointment,
                                    BindingResult result,
                                    @RequestParam("prescriptionId") int prescriptionId) {
        if (result.hasErrors()) {
            return "create-appointment";
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);
        if (prescription == null) {
            return "error-page";
        }

        appointment.setPrescription(prescription);
        repositoryClass.save(appointment);

        return "redirect:/appointments/";
    }

    @GetMapping("/{id}")
    public String viewAppointment(@PathVariable("id") int id, Model model) {
        Appointment appointment = repositoryClass.findById(id).orElse(null);
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        if (appointment == null) {
            return "error-page";
        }
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("appointment", appointment);
        return "show-appointment";
    }

    @PostMapping("/{id}")
    public String updateAppointment(@PathVariable("id") int id,
                                    @Valid @ModelAttribute("appointment") Appointment appointment,
                                    @RequestParam("prescriptionId") int prescriptionId,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return "show-appointment";
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);

        if (prescription == null) {
            return "error-page";
        }

        appointment.setId(id);
        appointment.setPrescription(prescription);
        repositoryClass.save(appointment);

        return "redirect:/appointments/";
    }


    @GetMapping("/{id}/edit")
    public String editAppointmentForm(@PathVariable("id") int id, Model model) {
        Appointment appointment = repositoryClass.findById(id).orElse(null);
        if (appointment == null) {
            return "error-page";
        }
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("appointment", appointment);
        return "edit-appointment";
    }

    @GetMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable("id") int id) {
        repositoryClass.deleteById(id);
        return "redirect:/appointments/";
    }
}
