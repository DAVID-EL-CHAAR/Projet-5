package com.safetyNet.safetyNetAlerts.repository;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository;
    private MedicalRecord record1;
    private MedicalRecord record2;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository = new MedicalRecordRepository();

        record1 = new MedicalRecord();
        record1.setFirstName("John");
        record1.setLastName("Doe");

        record2 = new MedicalRecord();
        record2.setFirstName("Jane");
        record2.setLastName("Doe");

        List<MedicalRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        
        medicalRecordRepository.setMedicalRecords(records);
    }


    @Test
    public void getAllMedicalRecordsShouldReturnAllRecords() {
        List<MedicalRecord> records = medicalRecordRepository.getAllMedicalRecords();

        assertEquals(2, records.size());
        assertEquals(record1, records.get(0));
        assertEquals(record2, records.get(1));
    }

    @Test
    public void getMedicalRecordByNameShouldReturnCorrectRecord() {
        Optional<MedicalRecord> record = medicalRecordRepository.getMedicalRecordByName("John", "Doe");

        assertTrue(record.isPresent());
        assertEquals(record1, record.get());
    }

    @Test
    public void addMedicalRecordShouldAddRecordIfNotExists() {
        MedicalRecord record3 = new MedicalRecord();
        record3.setFirstName("Alice");
        record3.setLastName("Smith");

        MedicalRecord addedRecord = medicalRecordRepository.addMedicalRecord(record3);

        assertEquals(record3, addedRecord);
        assertEquals(3, medicalRecordRepository.getAllMedicalRecords().size());
    }

    @Test
    public void addMedicalRecordShouldThrowExceptionIfExists() {
        assertThrows(RuntimeException.class, () -> medicalRecordRepository.addMedicalRecord(record1));
    }

    @Test
    public void deleteMedicalRecordShouldDeleteCorrectRecord() {
        medicalRecordRepository.deleteMedicalRecord("John", "Doe");

        assertEquals(1, medicalRecordRepository.getAllMedicalRecords().size());
        assertEquals(record2, medicalRecordRepository.getAllMedicalRecords().get(0));
    }

    @Test
    public void deleteMedicalRecordShouldThrowExceptionIfNotExists() {
        assertThrows(RuntimeException.class, () -> medicalRecordRepository.deleteMedicalRecord("Alice", "Smith"));
    }

    @Test
    public void updateMedicalRecordShouldUpdateCorrectRecord() {
        MedicalRecord updatedRecord = new MedicalRecord();
        updatedRecord.setFirstName("John");
        updatedRecord.setLastName("Doe");

        MedicalRecord record = medicalRecordRepository.updateMedicalRecord(record1, updatedRecord);

        assertEquals(updatedRecord, record);
    }
    
    @Test
    public void updateMedicalRecordShouldThrowExceptionIfNotExists() {
        MedicalRecord updatedRecord = new MedicalRecord();
        updatedRecord.setFirstName("Alic");
        updatedRecord.setLastName("Smit");

        assertThrows(RuntimeException.class, () -> medicalRecordRepository.updateMedicalRecord(updatedRecord, updatedRecord));
    }
}

