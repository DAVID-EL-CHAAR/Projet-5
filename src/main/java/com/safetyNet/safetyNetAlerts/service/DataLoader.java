package com.safetyNet.safetyNetAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyNet.safetyNetAlerts.model.Data;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DataLoader {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    @Autowired
    public DataLoader(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.firestationRepository = firestationRepository;
    }

    @PostConstruct
    public void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = new ClassPathResource("data.json");
        try {
            Data data = mapper.readValue(resource.getInputStream(), Data.class);

         
            personRepository.setPersons(data.getPersons());
            medicalRecordRepository.setMedicalRecords(data.getMedicalRecords());
            firestationRepository.setFirestations(data.getFirestations());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 