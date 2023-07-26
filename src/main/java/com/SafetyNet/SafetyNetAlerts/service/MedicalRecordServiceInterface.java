package com.SafetyNet.SafetyNetAlerts.service;

import com.SafetyNet.SafetyNetAlerts.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordServiceInterface {
    List<MedicalRecord> getAllMedicalRecords();
    MedicalRecord getMedicalRecordByNameAndFirstName(String name, String firstName);
    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);
    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
    void deleteMedicalRecord(String name, String firstName);
}
