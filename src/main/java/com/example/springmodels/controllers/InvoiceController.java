package com.example.springmodels.controllers;

import com.example.springmodels.models.Invoice;
import com.example.springmodels.models.Patient;
import com.example.springmodels.repos.InvoiceRepository;
import com.example.springmodels.repos.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invoices/")
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository, PatientRepository patientRepository) {

        this.invoiceRepository = invoiceRepository;
       this.patientRepository = patientRepository;

    }

    @GetMapping
    public String getInvoices(Model model) {
        List<Invoice> invoices = invoiceRepository.findAll();
        model.addAttribute("invoices", invoices);
        List<Patient> patients = patientRepository.findAll(); // получаем список пациентов
        model.addAttribute("patients", patients); // добавля
        return "all-invoices";
    }

    @GetMapping("/create-invoice")
    public String createInvoiceForm(Model model) {
        List<Patient> patients = patientRepository.findAll(); // получаем список пациентов
        model.addAttribute("patients", patients); // добавля
        model.addAttribute("invoice", new Invoice());
        return "create-invoice";
    }

    @PostMapping("/create-invoice")
    public String createInvoice(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult result,
                                @RequestParam("patient.id") int patientId) {
        if (result.hasErrors()) {
            return "create-invoice";
        }

        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return "error-page";
        }

        invoice.setPatient(patient);
        invoiceRepository.save(invoice);
        return "redirect:/invoices/";
    }


    @GetMapping("/{id}")
    public String viewInvoice(@PathVariable("id") int id, Model model) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        List<Patient> patients = patientRepository.findAll(); // получаем список пациентов
        if (invoice == null) {
            return "error-page";
        }
        model.addAttribute("invoice", invoice);
        model.addAttribute("patients", patients); // добавляем список пациентов в модель
        return "show-invoice";
    }

    @GetMapping("/{id}/edit")
    public String editInvoiceForm(@PathVariable("id") int id, Model model) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            return "error-page";
        }
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("invoice", invoice);
        model.addAttribute("patients", patients);
        return "edit-invoice";
    }

    @PostMapping("/{id}/edit")
    public String updateInvoice(@PathVariable("id") int id, @Valid @ModelAttribute("invoice") Invoice invoice, BindingResult result,
                                @RequestParam("patient.id") int patientId) {
        if (result.hasErrors()) {
            return "edit-invoice";
        }

        Invoice existingInvoice = invoiceRepository.findById(id).orElse(null);
        if (existingInvoice == null) {
            return "error-page";
        }

        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return "error-page";
        }

        existingInvoice.setPatient(patient);

        existingInvoice.setAmount(invoice.getAmount());
        existingInvoice.setIsPaid(invoice.getIsPaid());

        invoiceRepository.save(existingInvoice);

        return "redirect:/invoices/";
    }

    @GetMapping("/{id}/delete")
    public String deleteInvoice(@PathVariable("id") int id) {
        invoiceRepository.deleteById(id);
        return "redirect:/invoices/";
    }
}
