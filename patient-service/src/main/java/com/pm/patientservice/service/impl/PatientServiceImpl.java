package com.pm.patientservice.service.impl;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.dto.UpdatePatientRequest;
import com.pm.patientservice.exception.custom.EmailAlreadyExistException;
import com.pm.patientservice.exception.custom.InvalidDateOfBirth;
import com.pm.patientservice.exception.custom.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.helper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    public PatientServiceImpl(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }
    @Override
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::mapToDTO).toList();
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        validatePatient(patientRequestDto);
        Patient patient = patientRepository.save(PatientMapper.mapToPatient(patientRequestDto));

        //after the patient is created we are creating its billing account
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),patient.getName(),patient.getEmail());

        return PatientMapper.mapToDTO(patient);
    }

    @Override
    public PatientResponseDto updatePatient(UpdatePatientRequest patientRequestDto, UUID id) {
        if (patientRepository.existsById(id)) {

            validatePatient(patientRequestDto,id);

            Patient patient = patientRepository.findById(id).get();
            patient.setName(patientRequestDto.getName());
            patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
            patient.setAddress(patientRequestDto.getAddress());
            patient.setEmail(patientRequestDto.getEmail());
            patientRepository.save(patient);
            return PatientMapper.mapToDTO(patientRepository.findById(id).get());
        }
        else {
            throw new PatientNotFoundException(
                    "Patient with this id does not exist :- "+id);
        }

    }

    @Override
    public boolean deletePatient(UUID id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        else {
            throw new PatientNotFoundException("Patient with this id does not exist :- "+id);
        }
    }

    public void validatePatient(PatientRequestDto patientRequestDto) {
        if (patientRepository.existsPatientByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email" +
                    " already exist :- " + patientRequestDto.getEmail());
        }
        if (LocalDate.parse(patientRequestDto.getDateOfBirth()).isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirth("Date of birth cannot be in the future");
        }
    }
    //method overloading
    public void validatePatient(UpdatePatientRequest patientRequestDto, UUID id) {
        if (patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(),id)) {
            throw new EmailAlreadyExistException("A patient with this email" +
                    " already exist :- " + patientRequestDto.getEmail());
        }
        if (LocalDate.parse(patientRequestDto.getDateOfBirth()).isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirth("Date of birth cannot be in the future");
        }
    }
}
