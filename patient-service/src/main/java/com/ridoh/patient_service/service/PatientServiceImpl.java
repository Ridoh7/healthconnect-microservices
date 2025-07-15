package com.ridoh.patient_service.service;

import com.ridoh.patient_service.DTOs.PatientRequest;
import com.ridoh.patient_service.DTOs.PatientResponse;
import com.ridoh.patient_service.exception.ResourceNotFoundException;
import com.ridoh.patient_service.mapper.PatientMapper;
import com.ridoh.patient_service.model.Patient;
import com.ridoh.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = PatientMapper.toEntity(request);
        return PatientMapper.toResponse(patientRepository.save(patient));
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return PatientMapper.toResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PatientResponse> getAllPatients(String name, String gender, Pageable pageable) {
        Page<Patient> page = patientRepository.findAll(pageable); // Optionally filter by name/gender later
        return page.map(PatientMapper::toResponse);
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setFullName(request.getFullName());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setGender(request.getGender());
        patient.setDateOfBirth(request.getDateOfBirth());

        return PatientMapper.toResponse(patientRepository.save(patient));
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }
}
