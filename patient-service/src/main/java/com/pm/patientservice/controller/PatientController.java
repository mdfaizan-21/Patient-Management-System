package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.dto.UpdatePatientRequest;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient" , description = "API for managing patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get All patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDto));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientResponseDto> updatePatient(@Valid @RequestBody UpdatePatientRequest patientRequestDto,
                                                            @PathVariable UUID id) {
        return ResponseEntity.ok(patientService.updatePatient(patientRequestDto,id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        if (patientService.deletePatient(id))
            return new ResponseEntity<>("Patient Deleted Successfully", HttpStatus.OK);

        else
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);

    }

}
