package com.example.springmodels.repos;

import com.example.springmodels.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository   extends JpaRepository<Appointment, Integer> {
        List<Appointment> findByDescription(String descripton);
}