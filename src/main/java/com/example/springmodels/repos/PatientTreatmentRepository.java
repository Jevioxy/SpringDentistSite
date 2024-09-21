package com.example.springmodels.repos;

import com.example.springmodels.models.PatientTreatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientTreatmentRepository extends JpaRepository<PatientTreatment, Integer> {
}

