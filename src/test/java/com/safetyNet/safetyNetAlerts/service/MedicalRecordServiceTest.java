package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceTest {
    @InjectMocks
    MedicalRecordService medicalRecordService;

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    MedicalRecord medicalRecord1;
    MedicalRecord medicalRecord2;
  

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            
            medicalRecord1 = new MedicalRecord();
            medicalRecord1.setFirstName("David");
            medicalRecord1.setLastName("Chaar");

            medicalRecord2 = new MedicalRecord();
            medicalRecord2.setFirstName("Sophie");
            medicalRecord2.setLastName("Durand");
        }

        @Test
        public void testGetAllMedicalRecords() {
            when(medicalRecordRepository.getAllMedicalRecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));

            assertEquals(2, medicalRecordService.getAllMedicalRecords().size());
            verify(medicalRecordRepository, times(1)).getAllMedicalRecords();
        }

        @Test
        public void testGetMedicalRecordByName() {
            when(medicalRecordRepository.getMedicalRecordByName("David", "Chaar")).thenReturn(Optional.of(medicalRecord1));

            Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordByName("David", "Chaar");

            assertTrue(record.isPresent());
            assertEquals("David", record.get().getFirstName());
            assertEquals("Chaar", record.get().getLastName());
            verify(medicalRecordRepository, times(1)).getMedicalRecordByName("David", "Chaar");
        }

        @Test
        public void testAddMedicalRecord() {
            when(medicalRecordRepository.addMedicalRecord(medicalRecord1)).thenReturn(medicalRecord1);

            MedicalRecord record = medicalRecordService.addMedicalRecord(medicalRecord1);

            assertEquals("David", record.getFirstName());
            assertEquals("Chaar", record.getLastName());
            verify(medicalRecordRepository, times(1)).addMedicalRecord(medicalRecord1);
        }

        @Test
        public void testDeleteMedicalRecord() {
            doNothing().when(medicalRecordRepository).deleteMedicalRecord("David", "Chaar");
            medicalRecordService.deleteMedicalRecord("David", "Chaar");
            verify(medicalRecordRepository, times(1)).deleteMedicalRecord("David", "Chaar");
        }

        @Test
        public void testUpdateMedicalRecord() {
            when(medicalRecordRepository.updateMedicalRecord(medicalRecord1, medicalRecord2)).thenReturn(medicalRecord2);

            MedicalRecord record = medicalRecordService.updateMedicalRecord(medicalRecord1, medicalRecord2);

            assertEquals("Sophie", record.getFirstName());
            assertEquals("Durand", record.getLastName());
            verify(medicalRecordRepository, times(1)).updateMedicalRecord(medicalRecord1, medicalRecord2);
        }
    }
