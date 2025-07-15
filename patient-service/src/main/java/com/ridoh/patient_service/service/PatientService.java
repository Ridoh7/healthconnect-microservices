package com.ridoh.patient_service.service;

import com.ridoh.patient_service.DTOs.PatientRequest;
import com.ridoh.patient_service.DTOs.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(PatientRequest request);

    PatientResponse getPatientById(Long id);

    List<PatientResponse> getAllPatients();

    Page<PatientResponse> getAllPatients(String name, String gender, Pageable pageable);

    PatientResponse updatePatient(Long id, PatientRequest request);

    void deletePatient(Long id);
}
