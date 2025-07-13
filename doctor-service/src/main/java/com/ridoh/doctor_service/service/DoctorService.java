package com.ridoh.doctor_service.service;

import com.ridoh.doctor_service.dto.DoctorRequest;
import com.ridoh.doctor_service.dto.DoctorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
    Page<DoctorResponse> getAllDoctors(String name, String specialization, Pageable pageable);

}
