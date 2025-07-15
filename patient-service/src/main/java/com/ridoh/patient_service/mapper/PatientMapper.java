package com.ridoh.patient_service.mapper;

import com.ridoh.patient_service.DTOs.PatientRequest;
import com.ridoh.patient_service.DTOs.PatientResponse;
import com.ridoh.patient_service.model.Patient;

public class PatientMapper {

    public static Patient toEntity(PatientRequest request) {
        return Patient.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .active(true)
                .build();
    }

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .active(patient.isActive())
                .build();
    }
}
