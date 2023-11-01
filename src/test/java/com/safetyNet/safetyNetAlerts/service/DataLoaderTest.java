package com.safetyNet.safetyNetAlerts.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyNet.safetyNetAlerts.model.Data;
import com.safetyNet.safetyNetAlerts.repository.FirestationRepository;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {
    @Mock
    PersonRepository personRepository;

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @Mock
    FirestationRepository firestationRepository;

    @InjectMocks
    DataLoader dataLoader;

    @Test
    public void testLoadData() throws IOException {
        Resource resource = new ClassPathResource("data.json");  
        ObjectMapper mapper = new ObjectMapper();
        InputStream resourceInputStream = resource.getInputStream();
        Data testData = mapper.readValue(resourceInputStream, Data.class);

        dataLoader.loadData();

      
        verify(personRepository, times(1)).setPersons(testData.getPersons());
        verify(medicalRecordRepository, times(1)).setMedicalRecords(anyList());

        verify(firestationRepository, times(1)).setFirestations(anyList());
    }
}
