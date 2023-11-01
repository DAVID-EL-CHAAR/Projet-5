package com.safetyNet.safetyNetAlerts.repository;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository

public class MedicalRecordRepository {

    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }

    public Optional<MedicalRecord> getMedicalRecordByName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
                .findFirst();
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> existingRecord = getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (existingRecord.isPresent()) {
            throw new RuntimeException("Le dossier médical existe déjà.");
        }
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public void deleteMedicalRecord(String firstName, String lastName) {
        Optional<MedicalRecord> existingRecord = getMedicalRecordByName(firstName, lastName);
        if (existingRecord.isEmpty()) {
            throw new RuntimeException("Pas de dossier médical à supprimer.");
        }
        medicalRecords.removeIf(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName));
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord existingMedicalRecord, MedicalRecord updatedMedicalRecord) {
        int index = medicalRecords.indexOf(existingMedicalRecord);
        if (index >= 0) {
            updatedMedicalRecord.setFirstName(existingMedicalRecord.getFirstName());
            updatedMedicalRecord.setLastName(existingMedicalRecord.getLastName());
            medicalRecords.set(index, updatedMedicalRecord);
            return updatedMedicalRecord;
        } else {
            throw new RuntimeException("Impossible de trouver le dossier médical à mettre à jour");
        }
    }
    
    public List<MedicalRecord> getPersons() {
		return medicalRecords;
	}


	public void setMedicalRecords(List<MedicalRecord> medicalRecords ) {
		this.medicalRecords = medicalRecords;
	}
}

