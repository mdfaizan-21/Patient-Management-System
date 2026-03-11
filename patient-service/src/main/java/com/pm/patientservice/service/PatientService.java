package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;

import java.util.List;

public interface PatientService {
    List<PatientResponseDto>getAllPatients();
    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);
}
