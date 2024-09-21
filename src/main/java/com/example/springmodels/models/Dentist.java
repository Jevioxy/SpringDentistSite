package com.example.springmodels.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dentist")
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "ФИО врача не должно быть пустым")
    private String name;
    @NotBlank(message = "Специализация врача не должна быть пустой")
    private String specialization;
    @NotBlank(message = "Лицензионный номер не должен быть пустым")
    @Size(max = 7, message = "Лицензионный номер должен быть не более 7 символов")
    private String licenseNumber;
    @NotBlank(message = "Название клиники не должно быть пустым")
    private String clinicName;
    @OneToMany(mappedBy = "dentist")
    private List<Prescription> prescriptions;
    public Dentist(int id, String name, String specialization, String licenseNumber, String clinicName) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.clinicName = clinicName;
    }

    public Dentist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName)  {
        this.clinicName = clinicName;
    }
}
