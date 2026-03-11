package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.dto.UpdatePatientRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientResponseDto>getAllPatients();
    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);
    PatientResponseDto updatePatient(@Valid UpdatePatientRequest patientRequestDto, UUID id);

    boolean deletePatient(UUID id);
}
