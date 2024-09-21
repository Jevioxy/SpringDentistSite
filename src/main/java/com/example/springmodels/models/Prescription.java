package com.example.springmodels.models;

import javax.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "dentist_id")
    private Dentist dentist;
    @NotBlank(message = "Лекарсвтво не должен быть пустым")
    private String medicineName;
    @NotBlank(message = "Применения не должны быть пустыми")
    private String application;

    public Prescription(Dentist dentist, String medicineName, String application) {
        this.dentist = dentist;
        this.medicineName = medicineName;
        this.application = application;
    }

    public Prescription() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dentist getDentist() {
        return dentist;
    }

    public void setDentist(Dentist dentist) {
        this.dentist = dentist;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return id == that.id && Objects.equals(dentist, that.dentist) && Objects.equals(medicineName, that.medicineName) && Objects.equals(application, that.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dentist, medicineName, application);
    }
}