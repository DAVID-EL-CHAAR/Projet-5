package com.SafetyNet.SafetyNetAlerts.controller;

import com.SafetyNet.SafetyNetAlerts.model.MedicalRecord;
import com.SafetyNet.SafetyNetAlerts.service.MedicalRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{firstName}/{lastName}")
    public MedicalRecord getMedicalRecordByName(@PathVariable String firstName, @PathVariable String lastName) {
        return medicalRecordService.getMedicalRecordByName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("Unable to find MedicalRecord with given name and firstname"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public void deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        medicalRecordService.deleteMedicalRecord(firstName, lastName);
    }

    @PutMapping("/{firstName}/{lastName}")
    public MedicalRecord updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @RequestBody MedicalRecord updatedMedicalRecord) {
        MedicalRecord existingMedicalRecord = getMedicalRecordByName(firstName, lastName);
        return medicalRecordService.updateMedicalRecord(existingMedicalRecord, updatedMedicalRecord);
    }
}
