package com.ridoh.doctor_service.repository;

import com.ridoh.doctor_service.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findAllByFullNameContainingIgnoreCaseAndSpecializationContainingIgnoreCase(String fullName, String specialization, Pageable pageable);

}
