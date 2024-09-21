package com.example.springmodels.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.persistence.*;


@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Сумма оплаты не должна быть пустой")
    private double amount;

    @NotBlank(message = "Cтатус оплаты не должен быть пустым")
    private String isPaid;

    @ManyToOne
    @NotBlank(message = "ФИО пациента не должно быть пустым")
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Invoice() {
    }

    public Invoice(String patientId, double amount, String isPaid) {
        this.amount = amount;
        this.isPaid = isPaid;
    }
    public Patient getPatient() {
       return patient;
  }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }
}
