package com.ridoh.doctor_service.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String hospitalName;
    private boolean active;
}