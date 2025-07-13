package com.ridoh.doctor_service.mapper;

import com.ridoh.doctor_service.dto.DoctorRequest;
import com.ridoh.doctor_service.dto.DoctorResponse;
import com.ridoh.doctor_service.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mappings({
            @Mapping(target = "fullName", expression = "java(request.getFirstName() + \" \" + request.getLastName())"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "specialization", source = "specialization"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "hospitalName", source = "hospitalName"),
            @Mapping(target = "active", constant = "true")  // set true by default
    })
    Doctor toEntity(DoctorRequest request);

    DoctorResponse toResponse(Doctor doctor);
}
