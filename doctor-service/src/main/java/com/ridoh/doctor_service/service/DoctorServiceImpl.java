package com.ridoh.doctor_service.service;

import com.ridoh.doctor_service.dto.DoctorRequest;
import com.ridoh.doctor_service.dto.DoctorResponse;
import com.ridoh.doctor_service.mapper.DoctorMapper;
import com.ridoh.doctor_service.model.Doctor;
import com.ridoh.doctor_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setFullName(request.getFirstName()+ request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setEmail(request.getEmail());

        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
    @Override
    public Page<DoctorResponse> getAllDoctors(String fullName, String specialization, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAllByFullNameContainingIgnoreCaseAndSpecializationContainingIgnoreCase(
                fullName != null ? fullName : "",
                specialization != null ? specialization : "",
                pageable
        );
        return doctors.map(doctorMapper::toResponse);
    }

}
