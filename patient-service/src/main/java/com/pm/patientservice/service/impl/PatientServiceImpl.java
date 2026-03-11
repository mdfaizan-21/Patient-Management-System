package com.pm.patientservice.service.impl;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.exception.custom.EmailAlreadyExistException;
import com.pm.patientservice.exception.custom.InvalidDateOfBirth;
import com.pm.patientservice.helper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    PatientRepository patientRepository;
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    @Override
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::mapToDTO).toList();
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {

        if (patientRepository.existsPatientByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email"+
                    " already exist :- "+patientRequestDto.getEmail());
        }
        if (LocalDate.parse(patientRequestDto.getDateOfBirth()).isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirth("Date of birth cannot be in the future");
        }
        Patient patient = patientRepository.save(PatientMapper.mapToPatient(patientRequestDto));
        return PatientMapper.mapToDTO(patient);
    }
}
