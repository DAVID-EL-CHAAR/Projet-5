package com.safetyNet.safetyNetAlerts.service;

import com.safetyNet.safetyNetAlerts.model.Firestation;
import com.safetyNet.safetyNetAlerts.model.MedicalRecord;
import com.safetyNet.safetyNetAlerts.model.Person;
import com.safetyNet.safetyNetAlerts.model.PersonInfo;
import com.safetyNet.safetyNetAlerts.repository.FirestationRepository;
import com.safetyNet.safetyNetAlerts.repository.MedicalRecordRepository;
import com.safetyNet.safetyNetAlerts.repository.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FirestationServiceTest {
    
    @Mock
    private FirestationRepository firestationRepository;
    
    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    
   
    private FirestationService firestationService;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.firestationService = new FirestationService(firestationRepository, personRepository, medicalRecordRepository);
    }

    @Test
    public void testGetAllFirestations() {
        when(firestationRepository.getAllFirestations()).thenReturn(Arrays.asList(new Firestation()));
        List<Firestation> result = firestationService.getAllFirestations();
        assertEquals(1, result.size());
        verify(firestationRepository).getAllFirestations();
    }
    
    @Test
    public void testGetFirestationByAddress() {
        when(firestationRepository.getFirestationByAddress("address6")).thenReturn(Arrays.asList(new Firestation()));
        List<Firestation> result = firestationService.getFirestationByAddress("address6");
        assertEquals(1, result.size());
        verify(firestationRepository).getFirestationByAddress("address6");
    }
    
    @Test
    public void testGetFirestationsByStation() {
        // Given
        Firestation firestation1 = new Firestation();
        firestation1.setStation("1");
        Firestation firestation2 = new Firestation();
        firestation2.setStation("2");

        when(firestationRepository.getFirestationsByStation("1")).thenReturn(Arrays.asList(firestation1));

        // When
        List<Firestation> result = firestationService.getFirestationsByStation("1");

        // Then
        verify(firestationRepository).getFirestationsByStation("1");
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getStation());
    }

    
    @Test
    void testAddFirestation() {
        Firestation firestation = new Firestation();
        when(firestationRepository.addFirestation(any(Firestation.class))).thenReturn(firestation);

        Firestation result = firestationService.addFirestation(firestation);

        assertEquals(firestation, result);
    }

    @Test
    void testDeleteFirestation() {
        Mockito.doNothing().when(firestationRepository).deleteFirestation(anyString());
        firestationService.deleteFirestation("testAddress");
    }
    
    @Test
    public void testDeleteFirestationByStation() {
        // Given
        Firestation firestation1 = new Firestation();
        firestation1.setStation("1");
        Firestation firestation2 = new Firestation();
        firestation2.setStation("2");

        List<Firestation> firestations = new ArrayList<>(Arrays.asList(firestation1, firestation2));
        doAnswer(invocation -> {
            Object argument = invocation.getArgument(0);
            firestations.removeIf(firestation -> firestation.getStation().equals(argument));
            return null;
        }).when(firestationRepository).deleteFirestationByStation(anyString());

        // When
        firestationService.deleteFirestationByStation("1");

        // Then
        verify(firestationRepository).deleteFirestationByStation("1");
        assertEquals(1, firestations.size());
        assertEquals("2", firestations.get(0).getStation());
    }

    
    @Test
    void testUpdateFirestation() {
        Firestation existingFirestation = new Firestation();
        Firestation updatedFirestation = new Firestation();
        when(firestationRepository.updateFirestation(any(Firestation.class), any(Firestation.class))).thenReturn(updatedFirestation);

        Firestation result = firestationService.updateFirestation(existingFirestation, updatedFirestation);

        assertEquals(updatedFirestation, result);
    }

    @Test
    void testGetFirestationByNumberAndAddress() throws NotFoundException {
        // Créer une instance de Firestation
        Firestation firestation = new Firestation();
        firestation.setStation("testStation");
        firestation.setAddress("testAddress");

        // Programmer le mock pour retourner une liste avec notre Firestation
        when(firestationRepository.getAllFirestations()).thenReturn(Arrays.asList(firestation));

        // Appeler la méthode à tester
        Firestation result = firestationService.getFirestationByNumberAndAddress("testStation", "testAddress");

        // Vérifier le résultat
        assertEquals(firestation, result);
    }

    @Test
    void testGetFirestationByNumberAndAddress_NotFound() {
        // Programmer le mock pour retourner une liste vide
        when(firestationRepository.getAllFirestations()).thenReturn(Collections.emptyList());

        // Vérifier que NotFoundException est lancée
        assertThrows(NotFoundException.class, () -> {
            firestationService.getFirestationByNumberAndAddress("testStation", "testAddress");
        });
    }

    
    @Test
    public void testGetPersonsByStationNumber() {
        Firestation firestation = new Firestation();
        firestation.setStation("1");
        firestation.setAddress("address6");
        Person person = new Person();
        person.setFirstName("david");
        person.setLastName("chaar");
        person.setAddress("address6");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("david");
        medicalRecord.setLastName("chaar");
        medicalRecord.setBirthdate("01/01/2000");

        when(firestationRepository.getFirestationsByStation("1")).thenReturn(Arrays.asList(firestation));
        when(personRepository.getPersonsByAddress("address6")).thenReturn(Arrays.asList(person));
        when(medicalRecordRepository.getMedicalRecordByName("david", "chaar")).thenReturn(Optional.of(medicalRecord));
        List<PersonInfo> result = firestationService.getPersonsByStationNumber("1");
        
        assertEquals(1, result.size());
        PersonInfo personInfo = result.get(0);
        assertEquals("david", personInfo.getFirstName());
        assertEquals("chaar", personInfo.getLastName());
        assertEquals(23, personInfo.getAge());  

        verify(firestationRepository).getFirestationsByStation("1");
        verify(personRepository).getPersonsByAddress("address6");
        verify(medicalRecordRepository).getMedicalRecordByName("david", "chaar");
    }
    
  
    @Test
    void testCalculateAge() {
        LocalDate birthDate = LocalDate.of(1995, 1, 1);
        int expectedAge = Period.between(birthDate, LocalDate.now()).getYears();

        int actualAge = firestationService.calculateAge(birthDate);

        assertEquals(expectedAge, actualAge);
    }

    @Test
    void testGetAddressesByStationNumbers() {
        // Créer des instances de Firestation
        Firestation firestation1 = new Firestation();
        firestation1.setStation("station1");
        firestation1.setAddress("address1");

        Firestation firestation2 = new Firestation();
        firestation2.setStation("station2");
        firestation2.setAddress("address2");

        // Programmer le mock pour retourner des listes avec nos Firestations
        when(firestationRepository.getFirestationsByStation("station1")).thenReturn(Arrays.asList(firestation1));
        when(firestationRepository.getFirestationsByStation("station2")).thenReturn(Arrays.asList(firestation2));

        // Appeler la méthode à tester
        List<String> addresses = firestationService.getAddressesByStationNumbers(Arrays.asList("station1", "station2"));

        // Vérifier le résultat
        assertEquals(2, addresses.size());
        assertTrue(addresses.contains("address1"));
        assertTrue(addresses.contains("address2"));
    }

    @Test
    void testGetStationByAddress() {
        // Créer une instance de Firestation
        Firestation firestation = new Firestation();
        firestation.setStation("testStation");
        firestation.setAddress("testAddress");

        // Programmer le mock pour retourner une liste avec notre Firestation
        when(firestationRepository.getFirestationByAddress("testAddress")).thenReturn(Arrays.asList(firestation));

        // Appeler la méthode à tester
        String result = firestationService.getstationByAddress("testAddress");

        // Vérifier le résultat
        assertEquals("testStation", result);
    }

}
